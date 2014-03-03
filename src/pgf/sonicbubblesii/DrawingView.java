package pgf.sonicbubblesii;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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
	private int dotColor;
	private float brushSize = (float) 20.0;
	private float defaultBrushSize = (float) 20.0;
	private int ringSpeed = 3;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint, dotPaint, animDot;
	// drawing path
	private Path drawPath;
	// path opacity
	private List<Integer> alphas;
	private int alphaPath = 255;
	// canvas size
	static int width, height;
	public float scale;
	public boolean check;
	private String feedback;

	public static Dot[] dots;
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
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		drawPaint.setAlpha(alphaPath);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		canvasPaint.setAlpha(255);
		

		/*
		 * configure the style for the dots with dotPaint, except color which is
		 * individual and configured below
		 */
		dotPaint = new Paint();
		dotPaint.setStrokeWidth(brushSize * 2);
		dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		// drawPaint.setStrokeJoin(Paint.Join.ROUND);
		// drawPaint.setStrokeCap(Paint.Cap.ROUND);

		animDot = new Paint();
		animDot.setStrokeWidth(brushSize);
		animDot.setStyle(Paint.Style.STROKE);
		// animDot.setStrokeJoin(Paint.Join.ROUND);
		// animDot.setStrokeCap(Paint.Cap.ROUND);

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
			Dot dot = new Dot();
			// place the dot correctly in the canvas
			do {
				dot.setPosX();
				dot.setPosY();
			} while (checkDotLimits(dot.getPosX(), dot.getPosY(), dot.getRadius())
					|| checkDotCollision(n, dot.getPosX(), dot.getPosY()));
			dot.setSample(numSamples);
			dot.setSampleTriggered(false);
			dot.setColor();
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
				double distX, distY;
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
			int radius = dots[d].getRadius();
			int wave = dots[d].getRingRadius();
			/*
			 * The ring stops drawing if the user lifts finger and touches
			 * the screen again, because this calls the restartHand method which
			 * sets all sample-triggered to false again, so it's necessary to
			 * add a second condition. Also: create two functions to put the
			 * drawing of dots and rings in nice separated cleaned places
			 */
			// draw the dot animation
			animDot.setColor(dots[d].getColor());
			animDot.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), wave, animDot);
			// increase ring size
			if (dots[d].getWaveOn()) { 
				dots[d].setRingRadius(wave + ringSpeed);
				//
				brushSize -= 0.3;
				animDot.setStrokeWidth(brushSize);
			}
			// return ring to dot size
			if (wave > radius * 5) {
				dots[d].setRingRadius(radius);
				dots[d].setWaveOn(false);
				brushSize = defaultBrushSize;
			}
			// draw the dot
			dotPaint.setColor(dots[d].getColor());
			dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), radius, dotPaint);

		}
		canvas.drawPath(drawPath, drawPaint);
		invalidate();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// handle finger proximity to Dots for sound triggering
		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			alphas = new ArrayList<Integer>();
			restartHand();
			alphas.add(255);
			drawPaint.setAlpha(255);
			
			drawPath.moveTo(touchX, touchY);
			checkBubble(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			int historySize = event.getHistorySize();
			drawPath.lineTo(touchX, touchY);
			for (Integer i : alphas) {
				i = i - 5;
			}
			alphas.add(255);
			checkBubble(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			boolean answer = checkHand();
			displayToast(answer);
			updateScore(answer);
			drawPath.reset();
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

	private void displayToast(boolean answer) {
		// display a short message type: right/wrong
		feedback = answer ? getContext().getString(R.string.right) : getContext().getString(
				R.string.wrong);
		Toast t = Toast.makeText(getContext(), feedback, Toast.LENGTH_SHORT);
		t.show();
		Log.i(DrawingView.SB, feedback);

	}

	private void updateScore(boolean answer) {
		// update score
		// TODO Update Level and Round on screen (variables are not created yet)
		if (answer) {
			// update score, Level and Round
			GameActivity.presentScore += 10;
			GameActivity.Hand ++;
			GameActivity.Level = GameActivity.Hand % 4;
			GameActivity.Round = 1 + GameActivity.Hand / 4;
			// increase numDots by 1 every 4 points of score
			GameActivity.numDots = 4 + GameActivity.Hand / 4;
			// write score, level and round on screen
			GameActivity.getScoreTxt().setText(
					getContext().getString(R.string.tv_score) + (GameActivity.presentScore));
			GameActivity.getLevelTxt().setText(
					getContext().getString(R.string.tv_level) + (GameActivity.Level));
			GameActivity.getRoundTxt().setText(
					getContext().getString(R.string.tv_round) + (GameActivity.Round));
		}
	}

	public boolean checkHand() {
		// Check if the theHand is equal to theTheme
		check = false;
		for (int i = 0; i < theTheme.length; i++) {
			if (theHand[i] != theTheme[i]) {
				check = false;
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
			double x, y;
			x = touchX - eachDot.getPosX();
			y = touchY - eachDot.getPosY();
			double dist = Math.hypot(x, y);
			if ((dist < Dot.radius) && (eachDot.getSampleTriggered() == false)) {
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
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		setupDots(GameActivity.numDots, GameActivity.numSamples);
		invalidate();
		GameActivity.theme.playTheme(750, 1000);

	}

}
