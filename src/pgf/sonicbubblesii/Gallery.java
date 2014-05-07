package pgf.sonicbubblesii;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Gallery extends Activity implements OnClickListener, OnTouchListener{

	private ImageButton btnPrevious, btnNext;
	private RadioGroup radGr;
	private RadioButton radBtn1, radBtn2, radBtn3;
	private Animation in, out;
	private ImageView imgView1, imgView2;
	private int imgView_Width = 400; // TODO check on different density devices. Is it necessary?
	private int imgView_Height = 400;
	private int imgIndex = 0;
	private int indexRadBtnChecked = 0;
	private int visible; // which ImageView is visible, imgView1 or imgView2
	private int images_demo[] = { R.drawable.demo1, R.drawable.demo2, R.drawable.demo3 };

	// Log tags
	public final static String SB = "Sonic Bubbles II";
	public final static String SB_LifeCycle = "SB II LifeCycle";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		btnPrevious = (ImageButton) findViewById(R.id.btnPrev);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		imgView1 = (ImageView) findViewById(R.id.imgView1);
		imgView2 = (ImageView) findViewById(R.id.imgView2);
		radGr = (RadioGroup) findViewById(R.id.demo_RadioGroup);
		radBtn1 = (RadioButton) findViewById(R.id.demo_radio1);
		radBtn2 = (RadioButton) findViewById(R.id.demo_radio2);
		radBtn3 = (RadioButton) findViewById(R.id.demo_radio3);
		btnPrevious.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		imgView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), images_demo[imgIndex],
				imgView_Width, imgView_Height));
		visible = 1;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPrev:
			if (imgIndex == 0) {
				imgIndex = images_demo.length - 1;
				if (visible == 1) {
					imgView2.startAnimation(in);
					imgView2.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
							images_demo[imgIndex], imgView_Width, imgView_Height));
					visible = 2;
					} else {
						imgView2.startAnimation(out);
						imgView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
								images_demo[imgIndex], imgView_Width, imgView_Height));
						visible = 1;
					}
			} else {
				if (visible == 1) {
				imgView2.startAnimation(in);
				imgView2.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images_demo[--imgIndex], imgView_Width, imgView_Height));
				visible = 2;
				} else {
					imgView2.startAnimation(out);
					imgView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
							images_demo[--imgIndex], imgView_Width, imgView_Height));
					visible = 1;
				}
			}
			break;

		case R.id.btnNext:
			if (imgIndex == images_demo.length - 1) {
				imgIndex = 0;
				if (visible == 1) {
					imgView2.startAnimation(in);
					imgView2.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
							images_demo[imgIndex], imgView_Width, imgView_Height));
					visible = 2;
					} else {
						imgView2.startAnimation(out);
						imgView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
								images_demo[imgIndex], imgView_Width, imgView_Height));
						visible = 1;
					}
			} else {
				if (visible == 1) {
				imgView2.startAnimation(in);
				imgView2.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
						images_demo[++imgIndex], imgView_Width, imgView_Height));
				visible = 2;
				} else {
					imgView2.startAnimation(out);
					imgView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
							images_demo[++imgIndex], imgView_Width, imgView_Height));
					visible = 1;
				}
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
	    imgView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
	    		images_demo[imgIndex], imgView_Width, imgView_Height));
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
