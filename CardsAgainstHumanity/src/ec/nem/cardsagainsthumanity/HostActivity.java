package ec.nem.cardsagainsthumanity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class HostActivity extends Activity {

	static final int CHOOSE_CARD = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    Intent i = new Intent(HostActivity.this, ChooseCardActivity.class);
		startActivityForResult(i, CHOOSE_CARD);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == CHOOSE_CARD){
			if(resultCode == RESULT_OK){
				Log.d("asd", data.getExtras().getStringArray("chosen_card")[0]);
			}
		}
	}
	
	/*@Override
	public void onBackPressed() {
	   return;
	}*/
}
