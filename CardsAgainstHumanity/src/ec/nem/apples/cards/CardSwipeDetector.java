package ec.nem.apples.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class CardSwipeDetector extends SimpleOnGestureListener {
	private static final String TAG = "CardSwipeDetector"; 
	private static final int SCROLL_MIN_DISTANCE = 20;
	private final int SWIPE_MIN_DISTANCE;
	private final int SWIPE_THRESHOLD_VELOCITY;
	private final int SEND_THRESHOLD_VELOCITY;
	
	private Context context;
	private PortraitHandView cardView;
	
	public CardSwipeDetector(Context c, PortraitHandView portraitHandView){
		context = c;
		this.cardView = portraitHandView;
		
		ViewConfiguration vc = ViewConfiguration.get(c);
		SWIPE_MIN_DISTANCE = vc.getScaledTouchSlop();
		SWIPE_THRESHOLD_VELOCITY = vc.getScaledMinimumFlingVelocity();
		SEND_THRESHOLD_VELOCITY = vc.getScaledMinimumFlingVelocity();
	}

	@Override
	public boolean onFling(MotionEvent initial, MotionEvent current, float velocityX, float velocityY){
		if(Math.abs(velocityX) > Math.abs(velocityY)){
			// Go to the next card
			if(initial.getX() - current.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
		    	//Toast.makeText(context, "to next card!", Toast.LENGTH_SHORT).show();
		    	cardView.shiftCardsForward();
				return true;
			}
			
			// Go to the previous card
			else if(current.getX() - initial.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
				//Toast.makeText(context, "to previous card!", Toast.LENGTH_SHORT).show();
				cardView.shiftCardsBackward();
				return true;
			}
		}
		
		// Send card to host
		if(initial.getY() - current.getY() > SWIPE_MIN_DISTANCE && 
				Math.abs(velocityX) > SEND_THRESHOLD_VELOCITY){
			//Toast.makeText(context, "send card!", Toast.LENGTH_SHORT).show();
			CardView c = cardView.removeFirstCard();
			Intent data = new Intent();
			data.putExtra("chosen_card", new String[]{c.getName(), c.getDescription()});
			((Activity)context).setResult(Activity.RESULT_OK, data);
			((Activity)context).finish();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onScroll(MotionEvent initial, MotionEvent current, float distanceX, float distanceY){
		if(initial.getX() - current.getX() > SCROLL_MIN_DISTANCE){
			PortraitHandView.moveCardRelative(cardView.frontCard(), -(int)distanceX, 0);
		}
		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent e){
		return true;
	}
}
