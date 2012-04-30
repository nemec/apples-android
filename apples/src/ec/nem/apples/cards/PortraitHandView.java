package ec.nem.apples.cards;

import java.util.Deque;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import ec.nem.apples.R;

public class PortraitHandView extends HandView implements OnTouchListener{

	Deque<CardView> cardQueue;  // Front of queue is the back of the screen
	int MAX_HAND_SIZE;
	final int CARD_BORDER = 15;
	public static Point FRONT_TOP_LEFT;
	
	private GestureDetector cardSwipeDetector;

	
	public PortraitHandView(Context context) {
		super(context);
		init(context);
	}
	
	public PortraitHandView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    
    public PortraitHandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context){
    	cardSwipeDetector = new GestureDetector(new CardSwipeDetector(context, this));
		
		cardQueue = new LinkedList<CardView>();
		MAX_HAND_SIZE = (int)getResources().getInteger(R.integer.max_hand_size);
		
		//Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		FRONT_TOP_LEFT = new Point(
				CARD_BORDER * (MAX_HAND_SIZE + 3),
				CARD_BORDER * (MAX_HAND_SIZE + 1));//display.getWidth() / 2, display.getHeight() / 2);
		
		RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) generateDefaultLayoutParams();
		p.width = LayoutParams.FILL_PARENT;
		p.height = LayoutParams.FILL_PARENT;
		setLayoutParams(p);
		
		this.setOnTouchListener(this);
    }
	
	public CardView[] getCards(){
		return cardQueue.toArray(new CardView[0]);
	}
	
	public CardView frontCard(){
		return cardQueue.peekLast();
	}
	
	@Override
	public void addCard(String title, String text){
		CardView l = new CardView(getContext(), title, text);
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
	
	public CardView removeFirstCard(){
		CardView v = cardQueue.pollLast();
		if(v != null){
			shiftCardsForward();
		}
		return v;
	}
	
	public void flushZOrder(){
		for(CardView c : cardQueue){
			c.getView().bringToFront();
		}
	}
	
	public static void moveCardRelative(CardView c, int x, int y){
		moveCardRelative(c, x, y, 500);
	}
	
	public static void moveCardRelative(CardView c, int x, int y, int duration){
		if(c != null){
			LayoutParams params;
			params = (LayoutParams)c.getView().getLayoutParams();
			x += params.leftMargin;
			y += params.topMargin;
			moveCardAbsolute(c, x, y);
		}
	}
	
	public static void moveCardAbsolute(CardView c, int x, int y){
		moveCardAbsolute(c, x, y, 0);
	}
	
	public static void moveCardAbsolute(final CardView c, final int x, final int y, int duration){
		if(c != null){
			if(duration > 0){
				TranslateAnimation anim = new TranslateAnimation(0, x, 0, y);
				anim.setDuration(duration);
				anim.setFillAfter(true);
				c.getView().startAnimation(anim);
				anim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationEnd(Animation animation) {
						LayoutParams params;
						params = (LayoutParams)c.getView().getLayoutParams();
						params.leftMargin = x;
						params.topMargin = y;
						c.getView().setLayoutParams(params);
					}
	
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
	
					@Override
					public void onAnimationStart(Animation animation) {
					}
				});
			}
			else{
				LayoutParams params;
				params = (LayoutParams)c.getView().getLayoutParams();
				params.leftMargin = x;
				params.topMargin = y;
				c.getView().setLayoutParams(params);
			}
		}
	}
	
	public void shiftCardsForward(){
		CardView back = cardQueue.pollLast();
		if(back != null){
			shiftCards(back, CARD_BORDER);
		}
		// Animate card behind
		LayoutParams params;
		params = (LayoutParams)back.getView().getLayoutParams();
		TranslateAnimation anim = new TranslateAnimation(
				-(params.leftMargin + params.width), 0, 0, 0);
		anim.setDuration(300);
		anim.setFillAfter(false);
		back.getView().startAnimation(anim);
		
		cardQueue.addFirst(back);
		flushZOrder();
	}
	
	public void shiftCardsBackward(){		
		CardView front = cardQueue.pollFirst();
		if(front != null){
			shiftCards(front, -CARD_BORDER);
			moveCardRelative(front, CARD_BORDER, CARD_BORDER);
		}
		
		cardQueue.addLast(front);
		flushZOrder();
	}
	
	private void shiftCards(CardView extra, int distance){
		for(CardView v : cardQueue){
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
			PortraitHandView.moveCardAbsolute(cardQueue.peekLast(),
					FRONT_TOP_LEFT.x, FRONT_TOP_LEFT.y);
			return true;
		}
		return handled;
	}
}
