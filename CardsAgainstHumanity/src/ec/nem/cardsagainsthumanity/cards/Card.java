package ec.nem.cardsagainsthumanity.cards;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import ec.nem.cardsagainsthumanity.R;

public class Card implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3343812702904947639L;
	private FrameLayout view;
	private String title;
	private String text;

	public Card(Context context, String title, String text) {
		this.title = title;
		this.text = text;
		view = (FrameLayout)((Activity)context).getLayoutInflater().inflate(R.layout.cardview, null);
		((TextView)view.findViewById(R.id.title)).setText(title);
		((TextView)view.findViewById(R.id.text)).setText(text);
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getText(){
		return text;
	}
	
	public View getView(){
		return view;
	}
	
	/*public Card(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
		 
	public Card(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}*/
}
