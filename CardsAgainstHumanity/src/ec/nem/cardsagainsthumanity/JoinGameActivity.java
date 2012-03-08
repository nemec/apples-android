package ec.nem.cardsagainsthumanity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class JoinGameActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    Intent i = new Intent(JoinGameActivity.this, HostActivity.class);
		startActivity(i);
	}
}
