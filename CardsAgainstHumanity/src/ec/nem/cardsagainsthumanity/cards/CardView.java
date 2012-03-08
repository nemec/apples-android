package ec.nem.cardsagainsthumanity.cards;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

public abstract class CardView extends RelativeLayout {
	
	public CardView(Context context) {
		super(context);
	}
	
	public abstract Card[] getCards();
	
	public abstract void addCard(String title, String text);
}
