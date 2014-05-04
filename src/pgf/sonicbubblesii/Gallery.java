package pgf.sonicbubblesii;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher.ViewFactory;

public class Gallery extends Activity implements OnClickListener {

	private Button btnPrevious, btnNext;
	private Animation slide_in_left, slide_out_right;
	private ImageView imgView;
	private int imgIndex = 0;
	private int images[] = { R.drawable.eng, R.drawable.eng22, R.drawable.eng33 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		btnPrevious = (Button) findViewById(R.id.btnPrev);
		btnNext = (Button) findViewById(R.id.btnNext);
		imgView = (ImageView) findViewById(R.id.imgView1);
		btnPrevious.setOnClickListener(this);
		btnNext.setOnClickListener(this);

		imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), images[imgIndex],
				400, 400));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPrev:
			if (imgIndex == 0) {
				imgIndex = images.length - 1;
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[imgIndex], 400, 400));				
			} else {
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[--imgIndex], 400, 400));
			}
			break;

		case R.id.btnNext:
			if (imgIndex == images.length - 1) {
				imgIndex = 0;
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[imgIndex], 400, 400));
			} else {
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[++imgIndex], 400, 400));
			}
			break;
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	// Example of call to load an image into an ImageView using the above
	// methods to decode
	// mImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
	// R.id.myimage, 100, 100));
}
