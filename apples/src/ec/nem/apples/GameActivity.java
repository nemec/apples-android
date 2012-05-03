package ec.nem.apples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ec.nem.apples.generic.Card;
import ec.nem.apples.generic.Deck;
import ec.nem.apples.utils.Utils;
import ec.nem.bluenet.BluetoothNodeService;
import ec.nem.bluenet.BluetoothNodeService.LocalBinder;
import ec.nem.bluenet.Message;
import ec.nem.bluenet.MessageListener;

public class GameActivity extends Activity implements MessageListener {

	private static final String TAG = "GameActivity";
	private static int MAX_HAND_SIZE;
	private static final int DEALER_RESULT = 1;
	private static final int PLAYER_RESULT = 2;
	private static final int OUTCOME_RESULT = 3;
	protected BluetoothNodeService connectionService;
	protected boolean boundToService;
	
	public static final String SEED_KEY = "seed";
	
	private Deck adjDeck;
	private Deck nounDeck;
	private Map<String, Integer> seedMap;
	
	private boolean continueGame = false;
	private Handler uiHandler;
	
	private Random seed = null;
	private ArrayList<String> playerOrder = null;
	private ArrayList<Card> hand = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.gameview);
	    
	    uiHandler = new Handler();
	    
	    Button cont = (Button)findViewById(R.id.startButton);
	    cont.setOnClickListener(new View.OnClickListener() {
	    	@Override
			public void onClick(View v) {
				continueGame = true;
			}
		});
	    
	    MAX_HAND_SIZE = (int)getResources().getInteger(R.integer.max_hand_size);
	    
	    seedMap = new HashMap<String, Integer>();
	    playerOrder = new ArrayList<String>();
	    hand = new ArrayList<Card>();

	    setStatus("Binding to Service...");
	    Intent intent = new Intent(this, BluetoothNodeService.class);
    	bindService(intent, connectionState, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(boundToService){
			boundToService = false;
			unbindService(connectionState);
		}
			
			
	}
	
	private void setStatus(String s){
		TextView status = (TextView)findViewById(R.id.gameStatus);
		Log.v(TAG + "-status", s);
		status.setText(s);
	}
	
	private ServiceConnection connectionState = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            connectionService = binder.getService();
            connectionService.addMessageListener(GameActivity.this);
            //connectionService.addNodeListener(GameActivity.this);
            boundToService = true;
            setStatus("Bound to Service.");
            startGame();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            boundToService = false;
        }
    };
    
    private void startGame(){
    	setStatus("Loading cards...");
	    adjDeck = new Deck(Utils.getCardsFromXML(this, R.xml.adj));
	    nounDeck = new Deck(Utils.getCardsFromXML(this, R.xml.noun));
	    
	    seedMap.put(connectionService.getUsername(), new Random().nextInt(Integer.MAX_VALUE));
	    
	    new SeedTask().execute();
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == PLAYER_RESULT){
			if(resultCode == RESULT_OK){
				Card chosen = (Card)data.getExtras().getSerializable("chosen_card");
				hand.remove(chosen);
				if(boundToService){
					connectionService.broadcastMessage("card", chosen);
				}
				else {
					Log.e(TAG, "Tried to send card but not bound to service.");
				}
				Intent intent = new Intent(GameActivity.this, OutcomeActivity.class);
				startActivityForResult(intent, OUTCOME_RESULT);
			}
		}
		else if(requestCode == DEALER_RESULT || 
				requestCode == OUTCOME_RESULT){
			if(resultCode == RESULT_OK){
				// Skip dealer
				for(int x=1; x < playerOrder.size(); x++){
					Card c = nounDeck.remove();
					if(playerOrder.get(x).equals(connectionService.getUsername())){
						hand.add(c);
					}
				}
				Collections.shuffle(playerOrder, seed);
				startTurn();
			}
		}
	}

	@Override
	public void onMessageReceived(Message message) {
		if(message.getText().equals(SEED_KEY)){
			synchronized (seedMap) {
				seedMap.put(message.getTransmitterName(), (Integer)message.getData());
				final TextView playerCount = (TextView)findViewById(R.id.playerCount);
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						Log.d(TAG, "Updating player count... " + seedMap.size());
						playerCount.setText(""+seedMap.size());
					}
				});
			}
		}
	}
	
	private void buildPlayerList(){
		seed = new Random(Collections.max(seedMap.values()));
		for(Map.Entry<String, Integer> entry : entriesSortedByValues(seedMap)){
			playerOrder.add(entry.getKey());
		}
	}
	
	private void dealCards(){
		nounDeck.shuffleDeck(seed);
		adjDeck.shuffleDeck(seed);
		
		for(int x=0; x < playerOrder.size(); x++){
			ArrayList<Card> cards = new ArrayList<Card>();
			for(int y=0; y < MAX_HAND_SIZE; y++){
				cards.add(nounDeck.remove());
			}
			if(playerOrder.get(x).equals(connectionService.getUsername())){
				hand.addAll(cards);
			}
		}
	}
	
	private void startTurn(){
		Card adj = adjDeck.remove();
		// "First" player is always the dealer
		if(playerOrder.get(0) == connectionService.getUsername()){
			Intent intent = new Intent(GameActivity.this, DealerActivity.class);
			intent.putExtra(DealerActivity.EXTRA_ADJECTIVE, adj);
			intent.putExtra(DealerActivity.EXTRA_PLAYERS, playerOrder);
			startActivityForResult(intent, DEALER_RESULT);
		}
		else {
			Intent intent = new Intent(GameActivity.this, ChooseCardActivity.class);
			intent.putExtra(ChooseCardActivity.EXTRA_CARDS, hand);
			intent.putExtra(ChooseCardActivity.EXTRA_ADJECTIVE, adj);
			startActivityForResult(intent, PLAYER_RESULT);
		}
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e1.getValue().compareTo(e2.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
	private class SeedTask extends AsyncTask<Void, Void, Integer>{

		@Override
		protected Integer doInBackground(Void... params) {
			while(!continueGame){
		    	connectionService.broadcastMessage("seed",
					seedMap.get(connectionService.getUsername()));
		    	try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
				}
		    }
			return seedMap.size();
		}

		protected void onPostExecute(Integer result){
			setStatus("Connected to " + result + " users.");
			
			/*Log.d(TAG, "Playing users: "+seedMap.size());
		    for(String s: seedMap.keySet()){
		    	Log.d(TAG, s);
		    }*/
		    buildPlayerList();
		    if(playerOrder.size() < 2){
		    	// Network failed.
				AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
				builder.setMessage("Error: We're the only person playing the game.")
			       .setCancelable(false)
			       .setNeutralButton("OK", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
				});
				AlertDialog alert = builder.create();
				alert.show();
		    }
		    else{
			    dealCards();
			    startTurn();
		    }
		}
	}

}
