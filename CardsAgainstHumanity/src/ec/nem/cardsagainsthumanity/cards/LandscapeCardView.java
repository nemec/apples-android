package ec.nem.cardsagainsthumanity.cards;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import ec.nem.cardsagainsthumanity.R;

public class LandscapeCardView extends CardView implements OnTouchListener{

	private ArrayList<Card> cards;
	final int MAX_HAND_SIZE;
	final int CARD_BORDER = 15;
	public static Point FRONT_TOP_LEFT;
	
	private GestureDetector cardSwipeDetector;
	
	public LandscapeCardView(Context context) {
		super(context);
		
		cards = new ArrayList<Card>();
		cardSwipeDetector = new GestureDetector(new FlingGestureDetector(context, this));

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
	
	public Card removeCardAt(float x, float y){
		return null;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean handled = cardSwipeDetector.onTouchEvent(event);
		if(!handled &&
			(event.getAction() == MotionEvent.ACTION_CANCEL ||
			event.getAction() == MotionEvent.ACTION_UP)){
			/*PortraitCardView.moveCardAbsolute(cardQueue.peekLast(),
					FRONT_TOP_LEFT.x, FRONT_TOP_LEFT.y);*/
			return true;
		}
		return handled;
	}
	
	class FlingGestureDetector extends SimpleOnGestureListener{

		private Context context;
		private LandscapeCardView cardView;
		private final int SWIPE_MIN_DISTANCE;
		private final int SEND_THRESHOLD_VELOCITY;
		
		public FlingGestureDetector(Context context,
				LandscapeCardView landscapeCardView) {
			this.context = context;
			cardView = landscapeCardView;
			
			ViewConfiguration vc = ViewConfiguration.get(context);
			SWIPE_MIN_DISTANCE = vc.getScaledTouchSlop();
			SEND_THRESHOLD_VELOCITY = vc.getScaledMinimumFlingVelocity();
		}
		
		@Override
		public boolean onFling(MotionEvent initial, MotionEvent current, float velocityX, float velocityY){
			// Send card to host
			if(initial.getY() - current.getY() > SWIPE_MIN_DISTANCE && 
					Math.abs(velocityX) > SEND_THRESHOLD_VELOCITY){
				//Toast.makeText(context, "send card!", Toast.LENGTH_SHORT).show();
				Card c = cardView.removeCardAt(initial.getX(), initial.getY());
				Intent data = new Intent();
				data.putExtra("chosen_card", new String[]{c.getTitle(), c.getText()});
				((Activity)context).setResult(Activity.RESULT_OK, data);
				((Activity)context).finish();
				return true;
			}
			return false;
		}
		
	}

	@Override
	public void addCard(String title, String text) {
		cards.add(new Card(getContext(), title, text));
	}

	@Override
	public Card[] getCards() {
		return cards.toArray(new Card[0]);
	}	
}
