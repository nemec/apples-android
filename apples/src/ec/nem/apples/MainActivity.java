package ec.nem.apples;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ec.nem.bluenet.BluetoothNodeService;
import ec.nem.bluenet.BuildNetworkActivity;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	
	private static final int PORT_NUMBER = 9001;
	private static final int MINIMUM_NETWORK_SIZE = 2;
	private static final int RESULT_BUILD_NETWORK = 1204;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		Button join = (Button)findViewById(R.id.join);
		join.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText name = (EditText)findViewById(R.id.name);
				if(name.getText().length() == 0){
					Toast.makeText(MainActivity.this,
							"Cannot join with empty name.", 
							Toast.LENGTH_SHORT).show();
				}
				else{
					// Start up the network with the given username
					Intent i = new Intent(MainActivity.this, BuildNetworkActivity.class);
					i.putExtra(BuildNetworkActivity.EXTRA_USERNAME, name.getText().toString());
			    	i.putExtra(BuildNetworkActivity.EXTRA_PORT, PORT_NUMBER);
					i.putExtra(BuildNetworkActivity.EXTRA_MINIMUM_NETWORK_SIZE, MINIMUM_NETWORK_SIZE);
					startActivityForResult(i, RESULT_BUILD_NETWORK);
				}
			}
		});
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	// Make sure the service is stopped when the app is destroyed
    	Intent serviceIntent = new Intent(this, BluetoothNodeService.class); 
    	stopService(serviceIntent);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == RESULT_BUILD_NETWORK){
			if(resultCode == RESULT_OK){
				// Network is ready, start the game.
				Intent intent = new Intent(this, GameActivity.class);
				startActivity(intent);
			}
			else if(resultCode == RESULT_CANCELED){
				// Network failed.
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Error: Unable to connect to a network.")
				       .setCancelable(false)
				       .setNeutralButton("OK", null);
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
}