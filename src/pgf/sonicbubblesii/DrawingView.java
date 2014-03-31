package pgf.sonicbubblesii;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawingView extends View {

	// Log tags
	public final static String SB = "Sonic Bubbles II";


	// native variables:
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;
	private int paintColor = 0xffff0000;
	private float brushSize = (float) 20.0;
	private int ringSpeed = 3;
	// drawing and canvas paint
	private Paint fingerPaint, canvasPaint, dotPaint, animPaint, dotShadowPaint, animShadowPaint;
	// drawing path
	private Path fingerPath;
	// shadow offset
	private static int shadowOff = 10; // TODO Make it relative to canvas width
	private static int shadowAlpha = 100;
	// canvas size
	static int width, height;
	public float scale;
	public boolean check;
	private String feedback;

	public static Dot[] dots;
	// temp variables used in checkBubble, checkDotCollision, setUpDots, onTouchEvent, declared here to avoid garbage
	private double x, y;
	double distX, distY;
	
	float touchX, touchY;
	public static int[] theTheme;
	public static int[] theHand;

	// constructors
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
	}

	// methods
	private void setupDrawing() {
		// get drawing area setup for interaction:
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		canvasPaint.setAlpha(255);
		fingerPath = new Path();
		fingerPaint = new Paint();
		dotPaint = new Paint();
		dotShadowPaint = new Paint();
		animPaint = new Paint();
		animShadowPaint = new Paint();
		// finger drawing style
		fingerPaint.setColor(paintColor);
		fingerPaint.setAntiAlias(true);
		fingerPaint.setStrokeWidth(brushSize);
		fingerPaint.setStyle(Paint.Style.STROKE);
		fingerPaint.setStrokeJoin(Paint.Join.ROUND);
		fingerPaint.setStrokeCap(Paint.Cap.ROUND);
		fingerPaint.setPathEffect(new CornerPathEffect(20));
		// dot, ring animation and it's shadows's styles
		dotPaint.setStyle(Paint.Style.FILL);
		dotShadowPaint.setStyle(Paint.Style.FILL);
		dotShadowPaint.setColor(Color.GRAY);
		dotShadowPaint.setAlpha(shadowAlpha);
		animPaint.setStyle(Paint.Style.STROKE);
		animShadowPaint.setStyle(Paint.Style.STROKE);
		animShadowPaint.setColor(Color.GRAY);
		animShadowPaint.setAlpha(shadowAlpha);	
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
		width = w;
		height = h;
		Log.i(SB, "inside onSizeChanged Canvas in " + width + "x" + height);

		// Funny and crazy that setUpDots() must be placed here to get it
		// working properly!!
		setupDots(GameActivity.numDots, GameActivity.numSamples);

	}

	void setupDots(int numDots, int numSamples) {
		dots = new Dot[numDots];
		theTheme = new int[dots.length];
		theHand = new int[dots.length];
		for (int n = 0; n < dots.length; n++) {
			// place the dot correctly in the canvas
			Dot dot = new Dot();
			do {
				dot.setPosX();
				dot.setPosY();
			} while (checkDotLimits(dot.getPosX(), dot.getPosY(), dot.getRadius())
					|| checkDotCollision(n, dot.getPosX(), dot.getPosY()));
			dot.setSample(numSamples);
			dot.setSampleTriggered(false);
			dot.setColor();
			// dot.setRingStrokeWidth(20);
			dots[n] = dot;
			// populate both the Theme and the hand(this, with default 999)
			theTheme[n] = dot.getSample();
			theHand[n] = 999;
			Log.i(SB,
					"new Dot at (x" + Integer.toString(dot.getPosX()) + ", " + "y"
							+ Integer.toString(dot.getPosY()) + ") and sampleIndex "
							+ dot.getSample() + " trig=" + dot.getSampleTriggered());
		}
	}

	private boolean checkDotLimits(int tempPosX, int tempPosY, int dotRadius) {
		// returns true if dot is OUTSIDE the canvas
		if (tempPosX < dotRadius * 1.5)
			return true;
		else if (tempPosY < dotRadius * 1.5)
			return true;
		else if (tempPosX > (width - dotRadius * 1.5))
			return true;
		else if (tempPosY > (width - dotRadius * 1.5))
			return true;
		else
			return false;
	}

	private boolean checkDotCollision(int n, int tempPosX, int tempPosY) {
		// returns true if there is COLLISION
		if (n == 0) {
			return false;
		} else {
			for (int i = 0; i < n; i++) {
				distX = tempPosX - dots[i].getPosX();
				distY = tempPosY - dots[i].getPosY();
				double distXY = Math.hypot(distX, distY);
				if (distXY < dots[i].getRadius() * 3) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw DrawingView
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		// draw the dot objects
		for (int d = 0; d < dots.length; d++) {
			/*
			 * The ring stops drawing if the user lifts finger and touches the
			 * screen again, because this calls the restartHand method which
			 * sets all sample-triggered to false again, so it's necessary to
			 * add a second condition. Also: create two functions to put the
			 * drawing of dots and rings in nice separated cleaned places
			 */
			if (dots[d].getWaveOn()) {
				// draw the dot animation and it's shade
				animPaint.setColor(dots[d].getColor());
				animPaint.setStrokeWidth((float) dots[d].getRingStrokeWidth());
				animShadowPaint.setStrokeWidth((float) dots[d].getRingStrokeWidth());
				canvas.drawCircle(dots[d].getPosX() + shadowOff, dots[d].getPosY() + shadowOff, dots[d].getRingRadius(), animShadowPaint);
				canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), dots[d].getRingRadius(), animPaint);
				// increase ring size by 1 step (ringSpeed)
				dots[d].setRingRadius(dots[d].getRingRadius() + ringSpeed);
				// decrease ring stroke width
				dots[d].setRingStrokeWidth(dots[d].getRingStrokeWidth() - 0.3);
			}
			// return ring to dot size
			if (dots[d].getRingStrokeWidth() <= 0) {
				dots[d].setRingRadius(dots[d].getRadius());
				dots[d].setWaveOn(false);
				dots[d].resetRingStrokeWidth();
			}
			// draw the dot
			dotPaint.setColor(dots[d].getColor());
			// draw dot shade first
			canvas.drawCircle(dots[d].getPosX() + shadowOff, dots[d].getPosY() + shadowOff, dots[d].getRadius(), dotShadowPaint);
			canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), dots[d].getRadius(), dotPaint);

		}
		canvas.drawPath(fingerPath, fingerPaint);
		invalidate();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// handle finger proximity to Dots for sound triggering
		touchX = event.getX();
		touchY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			restartHand();
			fingerPath.moveTo(touchX, touchY);
			checkBubble(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			fingerPath.lineTo(touchX, touchY);
			checkBubble(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			boolean answer = checkHand();
			displayToast(answer, GameActivity.Life);
			updateScore(answer);
			fingerPath.reset();
			if (answer) {
				startNew();
			}
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	private void displayToast(boolean answer, int lifeCount) {
		// display a short message type: right/wrong on completed hands
		// first check that theHand is complete
		boolean display = false;
		for (int i = 0; i < theHand.length; i++) {
			if (theHand[i] == 999) {
				display = false;
				break;
			} else {
				display = true;
			}
		}
		if (display && lifeCount > 1) {
		// complete Hand, so display the Toast
		feedback = answer ? getContext().getString(R.string.right) : getContext()
				.getString(R.string.wrong);
		}
		if (display && lifeCount == 1) {
			feedback = getContext().getString(R.string.end_game);
		}
		Toast t = Toast.makeText(getContext(), feedback, Toast.LENGTH_SHORT);
		t.show();
		Log.i(DrawingView.SB, feedback);
	}

	private void updateScore(boolean answer) {
		if (answer) {
			// update score, Level and Round
			GameActivity.presentScore += 10;
			GameActivity.Hand++;
			GameActivity.Level++;
			if (GameActivity.Level > 4) {
				GameActivity.Level = 1;
				GameActivity.Round++;
				// increase numDots by 1 every 4 points of score
				GameActivity.numDots = 4 + GameActivity.Hand / 4;
			}
			writeScores();
		}
		else {
			GameActivity.Life --;
			writeScores();
		}
		
	}

	public void resetScores() {
		// reset score to default values
		GameActivity.presentScore = 10;
		GameActivity.Life = 5;
		GameActivity.Level = 1;
		GameActivity.Round = 1;
		GameActivity.Hand = 1;
		writeScores();
	}

	private void writeScores() {
		// write score, level and round on screen
		GameActivity.getScoreTxt().setText(
				getContext().getString(R.string.tv_score) + (GameActivity.presentScore));
		GameActivity.getLifeTxt().setText(
				getContext().getString(R.string.tv_life) + (GameActivity.Life));
		GameActivity.getLevelTxt().setText(
				getContext().getString(R.string.tv_level) + (GameActivity.Level));
		GameActivity.getRoundTxt().setText(
				getContext().getString(R.string.tv_round) + (GameActivity.Round));
	}

	public boolean checkHand() {
		// Check if the theHand is equal to theTheme
		check = false;
		for (int i = 0; i < theTheme.length; i++) {
			if (theHand[i] != theTheme[i]) {
				check = false;
				// loose points for error
				GameActivity.presentScore -= i + 1;
				if (GameActivity.presentScore <= 0) GameActivity.presentScore = 0;
				break;
			}
			check = true;
		}
		return check;
	}

	private void restartHand() {
		// sets all dots samples trigger values to false again
		// sets theHand to default value again
		for (Dot oneDot : dots) {
			oneDot.setSampleTriggered(false);
		}
		for (int i = 0; i < dots.length; i++) {
			theHand[i] = 999;
		}
	}

	private void checkBubble(float touchX, float touchY) {
		/*
		 * Handle proximity events and also associated animations like changing
		 * the color of the dots, size... Consider also using images and png
		 * based animations
		 */
		for (Dot eachDot : dots) {
			// To avoid duplicated sound triggerings
			x = touchX - eachDot.getPosX();
			y = touchY - eachDot.getPosY();
			double dist = Math.hypot(x, y);
			if ((dist < eachDot.getRadius() + eachDot.getRingStrokeWidth())
					&& (eachDot.getSampleTriggered() == false)) {
				/*
				 * If finger is close enough to the dot, and it's the first time
				 * in this hand, store dot in hand and fire it's sample
				 */
				for (int i = 0; i < dots.length; i++) {
					if (theHand[i] == 999) {
						theHand[i] = eachDot.getSample();
						break;
					}
				}
				Log.i(SB, "The finger is in dot with sample " + eachDot.getSample()
						+ " for the first time");

				GameActivity.doSound(eachDot.getSample());
				eachDot.setSampleTriggered(true);
				eachDot.setWaveOn(true);
			}
		}
	}

	public void startNew() {
		// drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		setupDots(GameActivity.numDots, GameActivity.numSamples);
		invalidate();
		GameActivity.theme.playTheme(750, 1000);

	}

}
