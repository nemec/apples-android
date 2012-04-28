package ec.nem.apples;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import ec.nem.apples.generic.Card;
import ec.nem.bluenet.BluetoothNodeService;
import ec.nem.bluenet.BluetoothNodeService.LocalBinder;
import ec.nem.bluenet.Message;
import ec.nem.bluenet.MessageListener;

public class DealerActivity extends Activity implements MessageListener {

	private static final String TAG = "DealerActivity";
	public static final String EXTRA_ADJECTIVE = "adj";
	protected BluetoothNodeService connectionService;
	protected boolean boundToService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dealerview);
    	
    	Card adjective  = (Card)getIntent().getSerializableExtra(EXTRA_ADJECTIVE);
    	TextView t = (TextView)findViewById(R.id.adjective);
    	t.setText(adjective.getWord());

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
	public void onMessageReceived(Message message) {
		Log.d(TAG, "Dealer received card: " + message.toString());
	}
}
