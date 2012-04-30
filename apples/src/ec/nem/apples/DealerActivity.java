package ec.nem.apples;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import ec.nem.apples.generic.Card;
import ec.nem.apples.utils.SubmittedListItem;
import ec.nem.bluenet.BluetoothNodeService;
import ec.nem.bluenet.BluetoothNodeService.LocalBinder;
import ec.nem.bluenet.Message;
import ec.nem.bluenet.MessageListener;

public class DealerActivity extends Activity implements MessageListener, OnItemClickListener{

	private static final String TAG = "DealerActivity";
	public static final String EXTRA_ADJECTIVE = "adj";
	public static final String EXTRA_PLAYERS = "players";
	protected BluetoothNodeService connectionService;
	protected boolean boundToService;
	
	private SimpleAdapter logAdapter;
	private ArrayList<Map<String, String>> submitted;
	private final String[] submitted_from = {"name", "word"};
	private final int[] submitted_to = {R.id.name, R.id.word};
	private ArrayList<String> players;
	
	Handler uiHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dealerview);
		
		submitted = new ArrayList<Map<String, String>>();
    	
    	Card adjective  = (Card)getIntent().getSerializableExtra(EXTRA_ADJECTIVE);
    	players = getIntent().getStringArrayListExtra(EXTRA_PLAYERS);
    	TextView t = (TextView)findViewById(R.id.adj);
    	t.setText(adjective.getWord());
    	
    	logAdapter = new SimpleAdapter(this, submitted, R.layout.submitted_item, submitted_from, submitted_to);
        ListView l = (ListView)findViewById(R.id.submitted_cards);
        l.setOnItemClickListener(this);
        //l.setEnabled(false);
        l.setAdapter(logAdapter);
        
        uiHandler = new Handler();

        Intent intent = new Intent(this, BluetoothNodeService.class);
    	bindService(intent, connectionState, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(connectionService != null){
			connectionService.removeMessageListener(this);
		}
		unbindService(connectionState);
	}
	
	
	private ServiceConnection connectionState = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            connectionService = binder.getService();
            connectionService.addMessageListener(DealerActivity.this);
            boundToService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            boundToService = false;
        }
    };


	@Override
	public void onMessageReceived(final Message message) {
		Log.d(TAG, "Dealer received card: " + message.toString());
		if(players != null &&
				message.getText().equals("card") &&
				players.indexOf(message.getTransmitterName()) >= 0){
			Card c = (Card)message.getData();
			submitted.add(new SubmittedListItem(message.getTransmitterName(), c));
			uiHandler.post(new Runnable() {
				@Override
				public void run() {
					logAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		SubmittedListItem i = (SubmittedListItem)parent.getItemAtPosition(pos);
		connectionService.broadcastMessage("winner", i.getName());
		setResult(RESULT_OK);
		finish();
	}
}
