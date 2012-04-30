package ec.nem.apples.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class HandView extends RelativeLayout {
	
	public HandView(Context context) {
		super(context);
	}
	
    public HandView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    
    public HandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	public abstract CardView[] getCards();
	
	public abstract void addCard(String title, String text);
}
