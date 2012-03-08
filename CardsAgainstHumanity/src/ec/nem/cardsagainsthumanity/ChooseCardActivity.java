package ec.nem.cardsagainsthumanity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import ec.nem.cardsagainsthumanity.cards.Card;
import ec.nem.cardsagainsthumanity.cards.CardView;
import ec.nem.cardsagainsthumanity.cards.LandscapeCardView;
import ec.nem.cardsagainsthumanity.cards.PortraitCardView;

public class ChooseCardActivity extends Activity {

	private static final String TAG = "ChooseCardActivity";
	CardView cardview;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        if(orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180){
        	cardview = new PortraitCardView(this);
        }
        else if(orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270){
        	cardview = new LandscapeCardView(this);
        }
        else{
        	Log.e(TAG, "Unknown Orientation: " + orientation);
        }
	    
        if(savedInstanceState != null){
		    Card[] cards = (Card[]) savedInstanceState.getSerializable("cards");
		    if(cards != null){
		    	for(Card c : cards){
		    		cardview.addCard(c.getTitle(), c.getText());
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
