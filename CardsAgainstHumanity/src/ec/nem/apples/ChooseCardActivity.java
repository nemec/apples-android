package ec.nem.apples;

import android.app.Activity;
import android.os.Bundle;
import ec.nem.apples.cards.CardView;
import ec.nem.apples.cards.HandView;
import ec.nem.apples.cards.PortraitHandView;

public class ChooseCardActivity extends Activity {

	private static final String TAG = "ChooseCardActivity";
	HandView cardview;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
    	cardview = new PortraitHandView(this);
	    
        if(savedInstanceState != null){
		    CardView[] cards = (CardView[]) savedInstanceState.getSerializable("cards");
		    if(cards != null){
		    	for(CardView c : cards){
		    		cardview.addCard(c.getName(), c.getDescription());
		    	}
		    }
        }
	    if(cardview.getCards().length == 0){
			cardview.addCard("stuff", "more");
			cardview.addCard("hello", "second");
	    }
		setContentView(cardview);
	}
	
	@Override
	public void onSaveInstanceState(Bundle b){
		b.putSerializable("cards", cardview.getCards());
	}

}
