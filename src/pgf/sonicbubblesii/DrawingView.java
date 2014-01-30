package pgf.sonicbubblesii;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

	// native variables:
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;
	private int paintColor = 0xffff0000;
	private int dotColor = 0xff000000;
	private float brushSize = (float) 20.0;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint, dotPaint;
	// drawing path
	private Path drawPath;
	Dot[] dots = new Dot[10];
	// Log tags
	private final String SB = "Sonic Bubbles II";

	// constructors
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
		setupDots();
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

		// configure the style for the dots with dotPaint
		dotPaint = new Paint();
		dotPaint.setColor(dotColor);
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

	}

	void setupDots() {
		for (int n = 0; n < 5; n++) {
			dots[n] = new Dot();
			Log.i(SB, "new Dot created");
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw DrawingView
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		// draw the dot objects
		for (int d = 0; d < 4; d++) {
			canvas.drawCircle(dots[d].getPosX(), dots[d].getPosY(), 30,
					dotPaint);
		}
		canvas.drawPath(drawPath, drawPaint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// handle user touch, and proximity to Dots for sound triggering
		float touchX = event.getX();
		float touchY = event.getY();

		// TODO: if touchX, touchY is close enough to each Dot...

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	public void startNew() {
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}

}
