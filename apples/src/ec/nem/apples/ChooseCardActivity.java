package ec.nem.apples;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import ec.nem.apples.cards.CardView;
import ec.nem.apples.cards.HandView;
import ec.nem.apples.cards.PortraitHandView;
import ec.nem.apples.generic.Card;

public class ChooseCardActivity extends Activity {

	private static final String TAG = "ChooseCardActivity";
	public static final String EXTRA_CARDS = "cards";
	public static final String EXTRA_ADJECTIVE = "adj";
	HandView handview;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview);
	    handview = (PortraitHandView)findViewById(R.id.handview);
	    
        //handview = new PortraitHandView(this);
		//setContentView(handview);
	    
        if(savedInstanceState != null){
		    CardView[] cards = (CardView[]) savedInstanceState.getSerializable("cards");
		    if(cards != null){
		    	for(CardView c : cards){
		    		handview.addCard(c.getName(), c.getDescription());
		    	}
		    }
        }
        else{
        	@SuppressWarnings("unchecked")
			ArrayList<Card> cards = (ArrayList<Card>)getIntent().getSerializableExtra(EXTRA_CARDS);
        	if(cards != null){
	        	for(Card c : cards){
	        		handview.addCard(c.getWord(), c.getDescription());
	        	}
        	}
        	Card adjective  = (Card)getIntent().getSerializableExtra(EXTRA_ADJECTIVE);
        	TextView adjView = (TextView)findViewById(R.id.adj);
        	adjView.setText(adjective.getWord());
        }
	}
	
	@Override
	public void onSaveInstanceState(Bundle b){
		b.putSerializable("cards", handview.getCards());
	}

}
