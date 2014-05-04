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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class Gallery extends Activity implements OnClickListener {

	private ImageSwitcher iSwitch;
	private int imgCount = 0;
	private int[] images = {R.drawable.eng, R.drawable.eng22, R.drawable.eng33};
	//private int[] images = {android.R.drawable.ic_dialog_alert, android.R.drawable.ic_lock_lock, android.R.drawable.ic_menu_more};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		iSwitch = (ImageSwitcher) findViewById(R.id.imageSwitcher1);

		Animation in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		iSwitch.setInAnimation(in);
		iSwitch.setOutAnimation(out);
		
		iSwitch.setFactory(new ViewFactory() {
			public View makeView() {
				ImageView myView = new ImageView(getApplicationContext());
				myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				myView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				return myView;
			}
		});
		iSwitch.setImageResource(images[imgCount]);
		iSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (imgCount == images.length - 1) {
					imgCount = 0;
					iSwitch.setImageResource(images[imgCount]);
				} else {
					iSwitch.setImageResource(images[++imgCount]);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
