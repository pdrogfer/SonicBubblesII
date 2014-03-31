package pgf.sonicbubblesii;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
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

	// Log tags
	public final static String SB = "Sonic Bubbles II";
	public final static String SB_LifeCycle = "SB II LifeCycle";

	// GUI
	Button listenAgain;
	DrawingView drawView;
	static Theme theme;
	public static int numDots, numSamples;

	// an arraylist of int to define wich samples are used in each level
	static List<Integer> chosenSamples = new ArrayList<Integer>();

	// scores
	private SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "Arithmetic_File";
	private static TextView scoreTxt, lifeTxt, levelTxt, roundTxt;
	public static int presentScore = 0;
	public static int Life = 5;
	public static int Hand = 1;
	public static int Level = 1;
	public static int Round = 1;
	private static String[] Modes;
	private static String Mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Number of Dots initialized here
		// TODO There is something wrong with which samples are chosen
		numDots = 4;
		numSamples = 4;
		Modes = getResources().getStringArray(R.array.string_array_levels);
		themeSetup();

		gamePrefs = getSharedPreferences(GAME_PREFS, 0);
		drawView = (DrawingView) findViewById(R.id.drawing);
		// drawView.setLayerType(drawView.LAYER_TYPE_SOFTWARE, null);
		listenAgain = (Button) findViewById(R.id.btnListenAgain);
		scoreTxt = (TextView) findViewById(R.id.scoreView);
		lifeTxt = (TextView) findViewById(R.id.lifeView);
		levelTxt = (TextView) findViewById(R.id.tvLevel);
		roundTxt = (TextView) findViewById(R.id.tvRound);
		listenAgain.setOnClickListener(this);
		
		if(savedInstanceState != null) {
			// there is saved instance state data
			Level = savedInstanceState.getInt("level");
			Round = savedInstanceState.getInt("round");
			Life = savedInstanceState.getInt("life");
			// Mode = savedInstanceState.getString("mode");
		}

		// display score
		scoreTxt.setText(getString(R.string.tv_score) + Integer.toString(presentScore));
		lifeTxt.setText(getString(R.string.tv_life) + Integer.toString(Life));
		levelTxt.setText(getString(R.string.tv_level) + Integer.toString(Level));
		roundTxt.setText(getString(R.string.tv_round) + Integer.toString(Round));

		// play first theme
		theme.playTheme(750, 1500);

		Log.i(SB_LifeCycle, "Game Activity On Create");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.general_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle selection on general_menu items
		switch (item.getItemId()) {
		case R.id.new_game:
			selectLevel();
			return true;
		case R.id.best_scores:
			Intent intentBest = new Intent(this, HighScores.class);
			startActivity(intentBest);
			return true;
		case R.id.hot_to_play:
			Intent intentHow = new Intent(this, HowToPlay.class);
			startActivity(intentHow);
			return true;
		case R.id.exit:
			// quit
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void themeSetup() {
		// retrieve data from IntroActivity's level selector and generate a
		// theme

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			numDots = extras.getInt("nBubbles");
			numSamples = extras.getInt("nSounds");
			Mode = extras.getString("mode");
		}
		
		theme = new Theme(numDots, numSamples);
		// depending in the number of samples, select which ones to use
		chooseSamples(theme.getNumSamples());
		Log.i(SB, "New Theme created");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnListenAgain:
			// play again the sequence, at 0.75 sec intervals
			theme.playTheme(750, 0);
			if (presentScore > 0) presentScore --;
			break;
		default:
			break;
		}

	}

	private void newGame() {
		// start a new game.
		// set score to 0, and numDots and numSamples to default value
		/*
		 * TODO This works, but is not accurate. It starts a new HAND, not a new
		 * GAME.
		 */
		
		drawView.startNew();
		drawView.resetScores();
		theme.playTheme(750, 1000);
	}
	
	private void selectLevel() {
		// choose level and set variables for the new game
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(this);
		levelDialog.setTitle(R.string.dialog_level_title);
		levelDialog.setItems(R.array.string_array_levels, new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// manage level choice
				switch (which) {
				case 0:
					numDots = 4;
					numSamples = 4;
					newGame();
					Mode = Modes[0];
					break;
				case 1:
					numDots = 4;
					numSamples = 7;
					newGame();
					Mode = Modes[1];
					break;
				case 2:
					numDots = 4;
					numSamples = 12;
					newGame();
					Mode = Modes[2];
					break;
				default:
					break;
				}
			}
		});
		levelDialog.show();	
	}

	private int getScore() {
		return presentScore;
	}

	private void setHighScore() {
		// set high score
		int exScore = getScore();
		if (exScore > 0) {
			// check valid value for exScore
			SharedPreferences.Editor scoreEdit = gamePrefs.edit();
			// create an editor object to write the scores
			// TODO Locale this format for english, french, etc
			DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
			String dateOutput = dateForm.format(new Date());
			String scores = gamePrefs.getString("highScores", "");
			if (scores.length() > 0) {
				// there are existing scores
				List<Score> scoreStrings = new ArrayList<Score>();
				String[] exScores = scores.split("\\|");
				for (String eSc : exScores) {
					String[] parts = eSc.split(" - ");
					scoreStrings.add(new Score(parts[0], parts[1], Integer.parseInt(parts[2])));
				}
				Score newScore = new Score(dateOutput, Mode, exScore);
				scoreStrings.add(newScore);
				Collections.sort(scoreStrings);
				StringBuilder scoreBuild = new StringBuilder("");
				for (int s = 0; s < scoreStrings.size(); s++) {
					if (s >= 10) {
						break; // we only want 10
					}
					if (s > 0) {
						scoreBuild.append("|"); // pipe separate the score
												// strings
					}
					scoreBuild.append(scoreStrings.get(s).getScoreText());
				}
				// write to prefs
				scoreEdit.putString("highScores", scoreBuild.toString());
				scoreEdit.commit();
			} else {
				// no existing scores
				scoreEdit.putString("highScores", "" + dateOutput + " - " + Mode + " - " + exScore);
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
	
	public static TextView getLifeTxt() {
		return lifeTxt;
	}
	
	public void setLifeTxt(TextView lifeTxt) {
		this.lifeTxt = lifeTxt;
	}

	public static TextView getLevelTxt() {
		return levelTxt;
	}

	public void setLevelTxt(TextView levelTxt) {
		this.levelTxt = levelTxt;
	}

	public static TextView getRoundTxt() {
		return roundTxt;
	}

	public void setRoundTxt(TextView roundTxt) {
		this.roundTxt = roundTxt;
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
			// C, D, E, F, G, A, B and C octave up
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(7);
			chosenSamples.add(9);
			chosenSamples.add(11);
			chosenSamples.add(12);
			break;
		case 9:
			break;
		case 10:
			break;
		case 11:
			break;
		case 12:
			// Full chromatic scale (no C up)
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(1);
			chosenSamples.add(2);
			chosenSamples.add(3);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(6);
			chosenSamples.add(7);
			chosenSamples.add(8);
			chosenSamples.add(9);
			chosenSamples.add(10);
			chosenSamples.add(11);
			break;
		}

	}

	public static void doSound(int sndId) {
		float volume = 1;
		if (IntroActivity.loaded) {
			IntroActivity.soundPool.play(chosenSamples.get(sndId) + 1, volume, volume, 1, 0, 1f);
			Log.i(SB, "sampleTriggered");
		}
	}

	@Override
	protected void onStart() {
		Log.i(SB_LifeCycle, "Game Activity On Start");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.i(SB_LifeCycle, "Game Activity On Restart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(SB_LifeCycle, "Game Activity On Resume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// set the present score
		setHighScore();
		super.onPause();
		Log.i(SB_LifeCycle, "Game Activity On Pause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Destroy the running game: save score and set scores to default start
		// TODO onStop is also called when HighScores activity is opened, not good
		presentScore = 0;
		Life = 5;
		Hand = 1;
		Level = 1;
		Round = 1;

		Log.i(SB_LifeCycle, "Game Activity On Stop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(SB_LifeCycle, "Game Activity On Destroy");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Implement this when support for landscape orientation
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// save score
		int exScore = getScore();
		savedInstanceState.putInt("score", exScore);
		savedInstanceState.putInt("life", Life);
		savedInstanceState.putInt("level", Level);
		savedInstanceState.putInt("round", Round);
		savedInstanceState.putString("mode", Mode);
		// superclass method
		super.onSaveInstanceState(savedInstanceState);
	}

}
