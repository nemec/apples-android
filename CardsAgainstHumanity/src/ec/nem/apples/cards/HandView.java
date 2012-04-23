package ec.nem.apples.cards;

import android.content.Context;
import android.widget.RelativeLayout;

public abstract class HandView extends RelativeLayout {
	
	public HandView(Context context) {
		super(context);
	}
	
	public abstract CardView[] getCards();
	
	public abstract void addCard(String title, String text);
}
