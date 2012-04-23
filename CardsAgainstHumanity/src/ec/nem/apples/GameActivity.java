package ec.nem.apples;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import ec.nem.apples.generic.Deck;
import ec.nem.apples.utils.Utils;
import ec.nem.bluenet.BluetoothNodeService;
import ec.nem.bluenet.BluetoothNodeService.LocalBinder;
import ec.nem.bluenet.Message;
import ec.nem.bluenet.MessageListener;

public class GameActivity extends Activity implements MessageListener{

	private static final String TAG = "GameActivity";
	static final int CHOOSE_CARD = 0;
	protected BluetoothNodeService connectionService;
	protected boolean boundToService;
	
	public static final String SEED_KEY = "seed";
	
	private Deck adjDeck;
	private Deck nounDeck;
	private Map<String, Integer> shuffleSeed;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    shuffleSeed = new TreeMap<String, Integer>();

	    setStatus("Binding to Service...");
	    Intent intent = new Intent(this, BluetoothNodeService.class);
    	bindService(intent, connectionState, Context.BIND_AUTO_CREATE);
	    
	    //Intent i = new Intent(GameActivity.this, ChooseCardActivity.class);
		//startActivityForResult(i, CHOOSE_CARD);
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
	    
	    shuffleSeed.put(connectionService.getUsername(), new Random().nextInt());
	    
	    // schedule send http://developer.android.com/reference/java/util/Timer.html#scheduleAtFixedRate(java.util.TimerTask, long, long)
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == CHOOSE_CARD){
			if(resultCode == RESULT_OK){
				Log.d("asd", data.getExtras().getStringArray("chosen_card")[0]);
			}
		}
	}

	@Override
	public void onMessageReceived(Message message) {
		Log.v(TAG, "Message received from " + 
				message.getTransmitterName() + 
				": " + message.getText());
		if(message.getText().equals(SEED_KEY)){
			shuffleSeed.put(message.getTransmitterName(), (Integer)message.getData());
		}
	}

}
