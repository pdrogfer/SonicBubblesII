package pgf.sonicbubblesii;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnClickListener {

	// GUI
	Button listenAgain, newGame;
	DrawingView drawView;
	static Theme theme;
	public static int numDots, numSamples;

	// an arraylist of int to define wich samples are used in each level
	static List<Integer> chosenSamples = new ArrayList<Integer>();

	// scores
	private SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "Arithmetic_File";
	private static TextView scoreTxt;
	public static int presentScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Number of Dots initialized here
		numDots = 4;
		numSamples = 7;
		themeSetup(numDots, numSamples);
		theme = new Theme(numDots, numSamples);
		// depending in the number of samples, select which ones to use
		chooseSamples(theme.getNumSamples());

		gamePrefs = getSharedPreferences(GAME_PREFS, 0);
		drawView = (DrawingView) findViewById(R.id.drawing);
		listenAgain = (Button) findViewById(R.id.btnListenAgain);
		scoreTxt = (TextView) findViewById(R.id.scoreView);
		newGame = (Button) findViewById(R.id.btnNewGame);
		listenAgain.setOnClickListener(this);
		newGame.setOnClickListener(this);

		// display score
		presentScore = 0;
		scoreTxt.setText("Score: " + Integer.toString(presentScore));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.general_menu, menu);
		presentScore = 0;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle selection on general_menu items
		switch (item.getItemId()) {
		case R.id.new_game:
			// open a new game
			return true;
		case R.id.best_scores:
			// show the best scores
			return true;
		case R.id.hot_to_play:
			// show activity_how
			Intent intent = new Intent(this, HowToPlay.class);
			startActivity(intent);
			return true;
		case R.id.exit:
			// quit
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void themeSetup(int nDots, int nSamples) {
		// generate a theme
		// theme = new Theme(nDots, nSamples);
		Log.i(DrawingView.SB, "New Theme created");
		// theme.playTheme(1000);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnListenAgain:
			// play again the sequence, at 1sec intervals
			theme.playTheme(1000);
			break;
		case R.id.btnNewGame:
			// start a new game
			drawView.startNew();
			break;
		default:
			break;
		}

	}

	private int getScore() {
		// returns the present score
		String scoreStr = getScoreTxt().getText().toString();
		return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ") + 1));
	}

	private void setHighScore() {
		// set high score
		int exScore = getScore();
		if (exScore > 0) {
			// check valid value for exScore
			SharedPreferences.Editor scoreEdit = gamePrefs.edit();
			// create an editor object to write the scores
			DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
			String dateOutput = dateForm.format(new Date());
			String scores = gamePrefs.getString("highScores", "");
			if (scores.length() > 0) {
				// there are existing scores
				List<Score> scoreStrings = new ArrayList<Score>();
				String[] exScores = scores.split("\\|");
				for (String eSc : exScores) {
					String[] parts = eSc.split(" - ");
					scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
				}
				Score newScore = new Score(dateOutput, exScore);
				scoreStrings.add(newScore);
				Collections.sort(scoreStrings);

				StringBuilder scoreBuild = new StringBuilder("");
				for (int s = 0; s < scoreStrings.size(); s++) {
					if (s > 10)
						break; // we only want 10
					if (s > 0) {
						scoreBuild.append("|"); // pipe separate the score
												// strings
						scoreBuild.append(scoreStrings.get(s).getScoreText());
					}
				}
				// write to prefs
				scoreEdit.putString("HighScores", scoreBuild.toString());
				scoreEdit.commit();
			} else {
				// no existing scores
				scoreEdit.putString("highScores", "" + dateOutput + " - " + exScore);
				scoreEdit.commit();
			}
		}

	}

	public static TextView getScoreTxt() {
		return scoreTxt;
	}

	public void setScoreTxt(TextView scoreTxt) {
		this.scoreTxt = scoreTxt;
	}

	private void chooseSamples(int levels) {
		/*
		 * Define the level to play in by setting the amount of sounds to use
		 * and wich ones. Create more levels: minor, pentatonic, blues,
		 * whole-tone, etc
		 */
		switch (levels) {
		case 4:
			// C, D, E, G
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(7);
			break;

		case 5:
			// C, D, E, G, A
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(7);
			chosenSamples.add(9);
			break;

		case 6:
			// C, D, E, F, G, A
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(7);
			chosenSamples.add(9);
			break;

		case 7:
			// C, D, E, F, G, A, B
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(7);
			chosenSamples.add(9);
			chosenSamples.add(11);
			break;

		case 8:
			break;
		}

	}

	public static void doSound(int sndId) {
		float volume = 1;
		if (IntroActivity.loaded) {
			IntroActivity.soundPool.play(chosenSamples.get(sndId) + 1, volume, volume, 1, 0, 1f);
			Log.i(DrawingView.SB, "sampleTriggered");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		IntroActivity.soundPool.release();
	}

}
