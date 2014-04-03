package pgf.sonicbubblesii;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class HighScores extends Activity {
	
	Typeface tf_thin, tf_reg;
	
	// Log tags
	public final static String SB = "Sonic Bubbles II";
	public final static String SB_LifeCycle = "SB II LifeCycle";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high);
		TextView header = (TextView)findViewById(R.id.intro);
		TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
		Typeface tf_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		Typeface tf_reg = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		scoreView.setTypeface(tf_thin);
		header.setTypeface(tf_reg);
		SharedPreferences scorePrefs = getSharedPreferences(GameActivity.GAME_PREFS, 0);
		String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
		Log.i(SB_LifeCycle, "savedScores: " + savedScores);
		
		StringBuilder scoreBuild = new StringBuilder("");
		for (String score : savedScores) {
			scoreBuild.append(score + "\n");
		}
		scoreView.setText(scoreBuild.toString());
	}

	
}
