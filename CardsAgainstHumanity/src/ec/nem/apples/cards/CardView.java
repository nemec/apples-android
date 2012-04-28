package ec.nem.apples.cards;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ec.nem.apples.R;

public class CardView implements Serializable{
	
	private static final long serialVersionUID = -3343812702904947639L;
	private RelativeLayout view;
	private String name;
	private String description;

	public CardView(Context context, String title, String text) {
		this.name = title;
		this.description = text;
		view = (RelativeLayout)((Activity)context).getLayoutInflater().inflate(R.layout.cardview, null);
		((TextView)view.findViewById(R.id.title)).setText(title);
		((TextView)view.findViewById(R.id.text)).setText(text);
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public View getView(){
		return view;
	}
	
	/*public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
		 
	public CardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}*/
}
