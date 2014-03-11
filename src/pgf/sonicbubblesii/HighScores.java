package pgf.sonicbubblesii;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class HighScores extends Activity {
	
	// Log tags
	public final static String SB = "Sonic Bubbles II";
	public final static String SB_LifeCycle = "SB II LifeCycle";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high);
		TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
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
