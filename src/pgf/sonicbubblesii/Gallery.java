package pgf.sonicbubblesii;

import android.app.Activity;
import android.content.Intent;
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
	private ViewFlipper vFlip;
	private Animation slide_in_left, slide_out_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		btnPrevious = (Button) findViewById(R.id.btnPrev);
		btnNext = (Button) findViewById(R.id.btnNext);
		vFlip = (ViewFlipper) findViewById(R.id.viewFlipper);

		slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		slide_out_right = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
		vFlip.setInAnimation(slide_in_left);
		vFlip.setOutAnimation(slide_out_right);	
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnPrev: 
			vFlip.showPrevious();
			break;
		case R.id.btnNext:
			vFlip.showNext();
			break;
		}
	}
}
