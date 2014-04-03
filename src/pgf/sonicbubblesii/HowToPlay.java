package pgf.sonicbubblesii;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class HowToPlay extends Activity{
	
	Typeface tf_thin;
	TextView instructions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_how);
		 instructions = (TextView) findViewById(R.id.how_to_play);
		 Typeface tf_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		 instructions.setTypeface(tf_thin);
	}

	
}
