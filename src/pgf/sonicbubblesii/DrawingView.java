package pgf.sonicbubblesii;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawingView extends View {

	// Log tags
	public final static String SB = "Sonic Bubbles II";

	private Bitmap canvasBitmap;
	private int paintColor = 0xffff0000;
	private float brushSize = (float) 20.0;
	private int ringSpeed = 3;
	// drawing and canvas paint
	private Paint fingerPaint, canvasPaint, dotPaint, animPaint, dotShadowPaint, animShadowPaint;
	private static Paint paint;
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
	/*
	 * temp variables used in checkBubble, checkDotCollision, setUpDots,
	 * onTouchEvent, checkHand, oneMoreGame, declared here to avoid garbage
	 */
	private double x, y;
	double distX, distY;
	float touchX, touchY;
	boolean equalLength, rightAnswer;
	int nDots, nSamples;
	double currTime, delTime;

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
		width = w;
		height = h;
		canvasBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.fondo1);
		canvasBitmap = Bitmap.createScaledBitmap(canvasBitmap, width, height, true);
		canvasBitmap = getRoundedCornerBitmap(canvasBitmap);
		new Canvas(canvasBitmap);
		Log.i(SB, "inside onSizeChanged Canvas in " + width + "x" + height);

		// Funny and crazy that setUpDots() must be placed here to get it
		// working properly!!
		setupDots(GameActivity.numDots, GameActivity.numSamples);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap
				.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 24; // Corners radius
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw DrawingView
		canvas.drawBitmap(canvasBitmap, 0, 0, null);
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
				canvas.drawCircle(dots[d].getPosX() + shadowOff, dots[d].getPosY() + shadowOff,
						dots[d].getRingRadius(), animShadowPaint);
				canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), dots[d].getRingRadius(),
						animPaint);
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
			canvas.drawCircle(dots[d].getPosX() + shadowOff, dots[d].getPosY() + shadowOff,
					dots[d].getRadius(), dotShadowPaint);
			canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), dots[d].getRadius(), dotPaint);

		}
		canvas.drawPath(fingerPath, fingerPaint);
		invalidate();

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
			checkHand();
			fingerPath.reset();
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	public void checkHand() {
		// First, check if LENGHT of theHand is equal to LENGTH of theTheme
		equalLength = false;
		for (int i = 0; i < theHand.length; i++) {
			if (theHand[i] == 999) {
				equalLength = false;
				break;
			} else {
				equalLength = true;
			}
		} // on equal length, check if theHand is the rigth answer
		rightAnswer = false;
		for (int j = 0; j < theTheme.length; j++) {
			if (theHand[j] != theTheme[j]) {
				rightAnswer = false;
				break;
			}
			rightAnswer = true;
		}
		updateScore(equalLength, rightAnswer);
		if (equalLength && rightAnswer) {
			// delayNewHand waits for 2 seconds before executing startNew()
			((GameActivity) getContext()).delayNewHand();
		}
	}

	private void updateScore(boolean equalLength, boolean answer) {
		if (equalLength == false && answer == false) {
			// incomplete Hand, do nothing
		} else if (equalLength == true && answer == false) {
			GameActivity.Life--;
			// show wrong message
			displayToast(false, GameActivity.Life);
		} else if (equalLength == true && answer == true) {
			// update score, Level and Round
			GameActivity.presentScore += dots.length;
			GameActivity.Hand++;
			GameActivity.Level++;
			if (GameActivity.Level > 4) {
				GameActivity.Level = 1;
				GameActivity.Round++;
				// Give an extra life every Round;
				GameActivity.Life++;
				// increase numDots by 1 every 4 points of score
				GameActivity.numDots = 4 + GameActivity.Hand / 4;
			}
			displayToast(true, GameActivity.Life);
		}
		// Show right message and write new scores on screen
		writeScores();
	}

	private void displayToast(boolean answer, int lifeCount) {
		// display a short message type: right/wrong
		if (lifeCount > 0) {
			feedback = answer ? getContext().getString(R.string.right) : getContext().getString(
					R.string.wrong);
			Toast t = Toast.makeText(getContext(), feedback, Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		} else {
			newGameDialog();
		}
	}

	private void newGameDialog() {
		// Write score on HighScores View and offer a new game
		GameActivity.setHighScore();
		AlertDialog.Builder oneMore = new AlertDialog.Builder(getContext());
		oneMore.setTitle(R.string.one_more);
		oneMore.setMessage(getContext().getString(R.string.tv_score) + (GameActivity.presentScore));
		oneMore.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// call dialog level selector for a new game
				oneMoreGame();
			}

		});
		oneMore.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// return to Intro Activity
				returnToIntro();
			}
		});
		AlertDialog dialog = oneMore.create();
		dialog.show();
	}

	protected void returnToIntro() {
		Intent intent = new Intent(getContext(), IntroActivity.class);
		((Activity) getContext()).startActivity(intent);
		resetScores();
	}

	private void oneMoreGame() {
		// choose level and set variables for the new game
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(getContext());
		levelDialog.setTitle(R.string.dialog_level_title);
		levelDialog.setItems(R.array.string_array_levels, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// manage level choice

				switch (which) {
				case 0:
					nDots = 4;
					nSamples = 4;
					break;
				case 1:
					nDots = 4;
					nSamples = 7;
					break;
				case 2:
					nDots = 4;
					nSamples = 12;
					break;
				default:
					break;
				}
				GameActivity.setMode(GameActivity.getModes()[which]);
				GameActivity.numDots = nDots;
				GameActivity.numSamples = nSamples;
				GameActivity.chooseSamples(GameActivity.numSamples);
				setupDots(GameActivity.numDots, GameActivity.numSamples);
				resetScores();
				invalidate();
				GameActivity.theme.setNumDots(GameActivity.numDots);
				GameActivity.theme.setNumSamples(GameActivity.numSamples);
				GameActivity.theme.playTheme(750, 1000);
			}
		});
		levelDialog.show();
	}

	public void resetScores() {
		// reset score to default values
		GameActivity.presentScore = 0;
		GameActivity.Life = 5;
		GameActivity.Level = 1;
		GameActivity.Round = 1;
		GameActivity.Hand = 1;
		writeScores();
	}

	public void writeScores() {
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

		// TODO wait some time before actually starting a new hand
		GameActivity.theme.setNumSamples(GameActivity.numSamples);
		setupDots(GameActivity.numDots, GameActivity.numSamples);
		invalidate();
		GameActivity.theme.playTheme(750, 1000);

	}

}
