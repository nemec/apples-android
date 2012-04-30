package ec.nem.apples;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.FrameLayout;
import ec.nem.bluenet.BluetoothNodeService;
import ec.nem.bluenet.BluetoothNodeService.LocalBinder;
import ec.nem.bluenet.Message;
import ec.nem.bluenet.MessageListener;

public class OutcomeActivity extends Activity implements MessageListener {

	private static final String TAG = "OutcomeActivity";
	protected BluetoothNodeService connectionService;
	protected boolean boundToService;
	Handler uiHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outcomeview);
		
        
        uiHandler = new Handler();

        Intent intent = new Intent(this, BluetoothNodeService.class);
    	bindService(intent, connectionState, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unbindService(connectionState);
	}
	
	private ServiceConnection connectionState = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            connectionService = binder.getService();
            connectionService.addMessageListener(OutcomeActivity.this);
            boundToService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            boundToService = false;
        }
    };
	
	@Override
	public void onMessageReceived(Message message) {
		if(message.getText().equals("winner")){
			final FrameLayout frame = (FrameLayout)findViewById(R.id.outcomeframe);
			if(message.getData().equals(
					connectionService.getUsername())){
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						frame.removeAllViews();
						getLayoutInflater().inflate(R.layout.winner, frame);
					}
				});
			}
			else {
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						frame.removeAllViews();
						getLayoutInflater().inflate(R.layout.loser, frame);
					}
				});
			}
		}
		
	}

	
}
