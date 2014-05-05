package pgf.sonicbubblesii;

import pgf.sonicbubblesii.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher.ViewFactory;

public class Gallery extends Activity implements OnClickListener {

	private ImageButton btnPrevious, btnNext;
	private RadioGroup radGr;
	private RadioButton radBtn1, radBtn2, radBtn3;
	private Animation slide_in_left, slide_out_right;
	private ImageView imgView;
	private int imgView_Width = 400;
	private int imgView_Height = 400;
	private int imgIndex = 0;
	private int indexRadBtnChecked = 0;
	private int images[] = { R.drawable.eng, R.drawable.eng22, R.drawable.eng33 };

	// Log tags
	public final static String SB = "Sonic Bubbles II";
	public final static String SB_LifeCycle = "SB II LifeCycle";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		btnPrevious = (ImageButton) findViewById(R.id.btnPrev);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		imgView = (ImageView) findViewById(R.id.imgView1);
		radGr = (RadioGroup) findViewById(R.id.demo_RadioGroup);
		radBtn1 = (RadioButton) findViewById(R.id.demo_radio1);
		radBtn2 = (RadioButton) findViewById(R.id.demo_radio2);
		radBtn3 = (RadioButton) findViewById(R.id.demo_radio3);
		btnPrevious.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), images[imgIndex],
				imgView_Width, imgView_Height));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPrev:
			if (imgIndex == 0) {
				imgIndex = images.length - 1;
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[imgIndex], imgView_Width, imgView_Height));
			} else {
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[--imgIndex], imgView_Width, imgView_Height));
			}
			break;

		case R.id.btnNext:
			if (imgIndex == images.length - 1) {
				imgIndex = 0;
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[imgIndex], imgView_Width, imgView_Height));
			} else {
				imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images[++imgIndex], imgView_Width, imgView_Height));
			}
			break;
		}
		indexRadBtnChecked = imgIndex;
		switch (indexRadBtnChecked) {
		case 0:
			radGr.check(R.id.demo_radio1);
			break;
		case 1:
			radGr.check(R.id.demo_radio2);
			break;
		case 2:
			radGr.check(R.id.demo_radio3);
			break;
		case -1:
			Toast.makeText(this, "Warning! no radioBtn selected", Toast.LENGTH_SHORT).show();
		}
	}
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.demo_radio1:
	            if (checked) imgIndex = 0;
	            break;
	        case R.id.demo_radio2:
	        	if (checked) imgIndex = 1;
	        		break;
	        case R.id.demo_radio3:
	        	if (checked) imgIndex = 2;
	        		break;
	    }
	    imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
	    		images[imgIndex], imgView_Width, imgView_Height));
	    indexRadBtnChecked = imgIndex;
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
