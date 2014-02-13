package pgf.sonicbubblesii;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawingView extends View {

	// native variables:
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;
	private int paintColor = 0xffff0000;
	private int dotColor;
	private float brushSize = (float) 20.0;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint, dotPaint;
	// drawing path
	private Path drawPath;
	// canvas size
	static int width, height;
	public float scale;
	// game size, as the number of dots. Init = 4
	private int gameSize = 4;
	Dot[] dots = new Dot[gameSize];

	// Log tags
	private final String SB = "Sonic Bubbles II";

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
		canvasPaint = new Paint(Paint.DITHER_FLAG);

		/* configure the style for the dots with dotPaint, except color
		 * which is individual and configured below
		 */
		dotPaint = new Paint();
		dotPaint.setStrokeWidth(brushSize * 2);
		dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);

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
		setupDots();

	}

	void setupDots() {
		for (int n = 0; n < dots.length; n++) {
			Dot dot = new Dot();
			// place the dot correctly in the canvas
			do {
				dot.setPosX();
			} while (dot.getPosX() < dot.getRadius() * 1.5
					|| dot.getPosX() > (width - dot.getRadius() * 1.5)
					|| checkCollisionX(n, dot.getPosX()));
			do {
				dot.setPosY();
			} while (dot.getPosY() < dot.getRadius() * 1.5
					|| dot.getPosY() > (height - dot.getRadius() * 1.5)
					|| checkCollisionY(n, dot.getPosY()));
			dot.setSample(GameActivity.levels);
			dot.setSampleTriggered(false);
			dot.setColor();
			dots[n] = dot;
			Log.i(SB,
					"new Dot at (x" + Integer.toString(dot.getPosX()) + ", " + "y"
							+ Integer.toString(dot.getPosY()) + ") and sampleIndex "
							+ dot.getSample() + " trig=" + dot.getSampleTriggered());
		}
	}

	private boolean checkCollisionX(int n, int tempPosX) {
		/*
		 * check collisions with other dots. This is a poor implementation. Use
		 * Math.hypoth to simplify and reduce the number of functions
		 */
		if (n == 0) {
			return false;
		} else {
			for (int i = 0; i < n; i++) {
				int dotsDistX = Math.abs(tempPosX - dots[i].getPosX());
				// Log.i(SB, "X distance: " + dotsDistX);
				if (dotsDistX <= Dot.radius * 2) {
					// Log.i(SB, "too close!! repeat");
					return true;
				}
			}
			return false;
		}

	}

	private boolean checkCollisionY(int n, int tempPosY) {
		// check collisions with other dots
		if (n == 0) {
			return false;
		} else {
			for (int i = 0; i < n; i++) {
				int dotsDistY = Math.abs(tempPosY - dots[i].getPosY());
				// Log.i(SB, "Y distance: " + dotsDistY);
				if (dotsDistY <= Dot.radius * 2) {
					// Log.i(SB, "too close!! repeat");
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
			dotPaint.setColor(dots[d].getColor());
			canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), dots[d].getRadius(), dotPaint);
		}
		canvas.drawPath(drawPath, drawPaint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// handle user touch, and proximity to Dots for sound triggering
		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			checkBubble(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			Log.i(SB, "");
			checkBubble(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			// to erase the hand on finger up
			//drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			restartHand();
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	private void restartHand() {
		// sets all dots samples trigger values to false again
		for (Dot oneDot : dots) {
			oneDot.setSampleTriggered(false);
		}
		
	}

	private void checkBubble(float touchX, float touchY) {
		/*
		 * Handle proximity events and also associated animations like changing
		 * the color of the dots, size... Consider also using images and png
		 * based animations
		 */
		for (Dot eachDot : dots) {
			/*
			 * TODO: improve performance, by avoiding duplicated sound
			 * triggerings
			 */
			double x, y;
			x = touchX - eachDot.getPosX();
			y = touchY - eachDot.getPosY();
			double dist = Math.hypot(x, y);
			if ((dist < Dot.radius) && (eachDot.getSampleTriggered() == false)) {
				/*
				 * If finger is close enough to the dot, and it's the first time
				 * in this hand, fire it's sample
				 */
				Log.i(SB, "The finger is in dot with sample " + eachDot.getSample()
						+ " for the first time");
				GameActivity.doSound(eachDot.getSample());
				eachDot.setSampleTriggered(true);
			}
		}
	}

	public void startNew() {
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		setupDots();
		invalidate();
	}

}
