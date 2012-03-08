package ec.nem.cardsagainsthumanity.cards;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import ec.nem.cardsagainsthumanity.R;

public class PortraitCardView extends CardView implements OnTouchListener{

	Deque<Card> cardQueue;  // Front of queue is the back of the screen
	final int MAX_HAND_SIZE;
	final int CARD_BORDER = 15;
	public static Point FRONT_TOP_LEFT;
	
	private GestureDetector cardSwipeDetector;

	
	public PortraitCardView(Context context) {
		super(context);
		
		cardSwipeDetector = new GestureDetector(new CardSwipeDetector(context, this));
		
		cardQueue = new LinkedList<Card>();
		MAX_HAND_SIZE = (int)getResources().getInteger(R.integer.max_hand_size);
		
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Log.d("a", display.getWidth() + " " + display.getHeight());
		FRONT_TOP_LEFT = new Point(
				CARD_BORDER * (MAX_HAND_SIZE + 3),
				CARD_BORDER * (MAX_HAND_SIZE + 1));//display.getWidth() / 2, display.getHeight() / 2);
		
		LayoutParams p = (LayoutParams) generateDefaultLayoutParams();
		p.width = LayoutParams.FILL_PARENT;
		p.height = LayoutParams.FILL_PARENT;
		setLayoutParams(p);
		
		this.setOnTouchListener(this);
	}
	
	public Card[] getCards(){
		return cardQueue.toArray(new Card[0]);
	}
	
	public Card frontCard(){
		return cardQueue.peekLast();
	}
	
	@Override
	public void addCard(String title, String text){
		Card l = new Card(getContext(), title, text);
		int size = cardQueue.size();
		if(size < MAX_HAND_SIZE){
			cardQueue.addFirst(l);
			addView(l.getView());
			moveCardAbsolute(l,
					FRONT_TOP_LEFT.x - CARD_BORDER * size,
					FRONT_TOP_LEFT.y - CARD_BORDER * size);
		}
		flushZOrder();
	}
	
	public Card removeFirstCard(){
		Card v = cardQueue.pollLast();
		if(v != null){
			shiftCardsForward();
		}
		return v;
	}
	
	public void flushZOrder(){
		for(Card c : cardQueue){
			c.getView().bringToFront();
		}
	}
	
	public static void moveCardRelative(Card c, int x, int y){
		moveCardRelative(c, x, y, 500);
	}
	
	public static void moveCardRelative(Card c, int x, int y, int duration){
		if(c != null){
			/*TranslateAnimation anim = new TranslateAnimation(0, x, 0, y);
			anim.setDuration(duration);
			anim.setFillAfter(false);
			c.startAnimation(anim);*/
		
			LayoutParams params;
			params = (LayoutParams)c.getView().getLayoutParams();
			params.leftMargin += x;
			params.topMargin += y;
			c.getView().setLayoutParams(params);
		}
	}
	
	public static void moveCardAbsolute(Card c, int x, int y){
		moveCardAbsolute(c, x, y, 500);
	}
	
	public static void moveCardAbsolute(Card c, int x, int y, int duration){
		if(c != null){
			/*TranslateAnimation anim = new TranslateAnimation(0, x, 0, y);
			anim.setDuration(duration);
			anim.setFillAfter(false);
			c.startAnimation(anim);*/
			
			LayoutParams params;
			params = (LayoutParams)c.getView().getLayoutParams();
			params.leftMargin = x;
			params.topMargin = y;
			c.getView().setLayoutParams(params);
		}
	}
	
	public void shiftCardsForward(){
		Card back = cardQueue.pollLast();
		if(back != null){
			shiftCards(back, CARD_BORDER);
		}
		cardQueue.addFirst(back);
		flushZOrder();
	}
	
	public void shiftCardsBackward(){
		//Card back = cardQueue.pollLast();
		Card front = cardQueue.pollFirst();
		if(front != null){
			shiftCards(front, -CARD_BORDER);
			moveCardRelative(front, CARD_BORDER, CARD_BORDER);
		}
		cardQueue.addLast(front);
		flushZOrder();
	}
	
	private void shiftCards(Card extra, int distance){
		for(Card v : cardQueue){
			moveCardRelative(v, distance, distance);
		}
		
		moveCardAbsolute(extra, 0, 0);
		int size = cardQueue.size();
		moveCardAbsolute(extra,
				FRONT_TOP_LEFT.x - CARD_BORDER * size,
				FRONT_TOP_LEFT.y - CARD_BORDER * size);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean handled = cardSwipeDetector.onTouchEvent(event);
		if(!handled &&
			(event.getAction() == MotionEvent.ACTION_CANCEL ||
			event.getAction() == MotionEvent.ACTION_UP)){
			PortraitCardView.moveCardAbsolute(cardQueue.peekLast(),
					FRONT_TOP_LEFT.x, FRONT_TOP_LEFT.y);
			return true;
		}
		return handled;
	}
}
