package pgf.sonicbubblesii;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ShareActionProvider;
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
	private static TextView scoreTxt, lifeTxt, levelTxt, roundTxt;
	Typeface tf2;

	// an arraylist of int to define which samples are used in each level
	static List<Integer> chosenSamples = new ArrayList<Integer>();
	// to store last sample played (to stop it in case it isn't already
	// finished). None == -1
	static int isPlaying = 0;

	// scores
	private static SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "Arithmetic_File";
	public static int presentScore = 0;
	public static int Life = 5;
	public static int Hand = 1;
	public static int Level = 1;
	public static int Round = 1;
	private static String[] Modes;
	private static String Mode;
	private static String shareText = "empty text";
	private static String shareText_sub1, shareText_sub2, shareText_web;
	private static Intent intentShare, intentShareUpdated;
	private static ShareActionProvider myShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Number of Dots initialized here
		numDots = 4;
		numSamples = 3;
		setModes(getResources().getStringArray(R.array.string_array_levels));
		themeSetup();

		gamePrefs = getSharedPreferences(GAME_PREFS, 0);
		drawView = (DrawingView) findViewById(R.id.drawing);
		listenAgain = (Button) findViewById(R.id.btnListenAgain);
		scoreTxt = (TextView) findViewById(R.id.scoreView);
		lifeTxt = (TextView) findViewById(R.id.lifeView);
		levelTxt = (TextView) findViewById(R.id.tvLevel);
		roundTxt = (TextView) findViewById(R.id.tvRound);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		listenAgain.setTypeface(tf2);
		scoreTxt.setTypeface(tf2);
		lifeTxt.setTypeface(tf2);
		levelTxt.setTypeface(tf2);
		roundTxt.setTypeface(tf2);

		listenAgain.setOnClickListener(this);

		if (savedInstanceState != null) {
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
		theme.playTheme(850, 1500);

		Log.i(SB_LifeCycle, "Game Activity On Create");

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu depending on API version; this adds items to the
		// action bar if it is present.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getMenuInflater().inflate(R.menu.general_menu, menu);
			MenuItem item = menu.findItem(R.id.menu_item_share);
			myShareActionProvider = (ShareActionProvider) item.getActionProvider();
			Intent intentShare = new Intent();
			intentShare.setAction(Intent.ACTION_SEND);
			shareText_sub1 = getString(R.string.share_txt_1);
			shareText_sub2 = getString(R.string.share_txt_2);
			shareText_web = getString(R.string.sonic_bubbles_web);
			shareText = shareText_sub1 + presentScore + shareText_sub2 + shareText_web;
			intentShare.putExtra(Intent.EXTRA_TEXT, shareText);
			intentShare.setType("text/plain");
			myShareActionProvider.setShareIntent(intentShare);
		} else {
			getMenuInflater().inflate(R.menu.general_menu_api_10, menu);
		}
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
		case R.id.how_to_play:
			Intent intentGallery = new Intent(this, Gallery.class);
			startActivity(intentGallery);
			return true;
		case R.id.exit:
			Intent intentIntro = new Intent(this, IntroActivity.class);
			startActivity(intentIntro);
			return true;
		case R.id.menu_item_share:
			// handled in OnCreatedOptionsMenu
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Call to update the share intent
	@SuppressLint("NewApi")
	public static void updateShareIntent() {
		if (myShareActionProvider != null) {
			Intent intentShareUpdated = new Intent();
			intentShareUpdated.setAction(Intent.ACTION_SEND);
			shareText = shareText_sub1 + presentScore + shareText_sub2 + shareText_web;
			intentShareUpdated.putExtra(Intent.EXTRA_TEXT, shareText);
			intentShareUpdated.setType("text/plain");
			myShareActionProvider.setShareIntent(intentShareUpdated);
		}
	}

	private void themeSetup() {
		// retrieve data from IntroActivity's level selector and generate a
		// theme

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			numDots = extras.getInt("nBubbles");
			numSamples = extras.getInt("nSounds");
			presentScore = extras.getInt("score");
			Life = extras.getInt("nLive");
			Level = extras.getInt("level");
			Round = extras.getInt("round");
			Hand = extras.getInt("hand");
			setMode(extras.getString("mode"));
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
			// play again the sequence, at 0.85 sec intervals
			theme.playTheme(850, 0);
			if (presentScore > 0)
				presentScore -= 10;
			drawView.writeScores();
			break;
		default:
			break;
		}

	}

	private void newGame() {
		// start a new game.
		// set score to 0, and numDots and numSamples to default value
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
					numSamples = 3;
					Life = 5;
					break;
				case 1:
					numDots = 4;
					numSamples = 7;
					Life = 5;
					break;
				case 2:
					numDots = 4;
					numSamples = 13;
					Life = 5;
					break;
				default:
					break;
				}
				setMode(getModes()[which]);
				chooseSamples(numSamples);
				newGame();
			}
		});
		levelDialog.show();
	}

	private static int getScore() {
		return presentScore;
	}

	public static void setHighScore() {
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
					scoreStrings.add(new Score(parts[0], parts[1], Integer.parseInt(parts[2])));
				}
				Score newScore = new Score(dateOutput, getMode(), exScore);
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
				scoreEdit.putString("highScores", "" + dateOutput + " - " + getMode() + " - "
						+ exScore);
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

	public static void chooseSamples(int levels) {
		/*
		 * Define the level to play in by setting the amount of sounds to use
		 * and wich ones. Create more levels: minor, pentatonic, blues,
		 * whole-tone, etc
		 */
		switch (levels) {
		case 3:
			// C, E, G
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(4);
			chosenSamples.add(7);
			break;
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
			// C, D, E, G, A, B
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(7);
			chosenSamples.add(9);
			chosenSamples.add(11);
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
			// C, D, E, F, G, A, B and C octave up (C')
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
			// C, D, E, F, G, A, Bb, B, C'
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(7);
			chosenSamples.add(9);
			chosenSamples.add(10);
			chosenSamples.add(11);
			chosenSamples.add(12);
			break;
		case 10:
			// C, D, E, F, F#, G, A, Bb, B, C'
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(6);
			chosenSamples.add(7);
			chosenSamples.add(9);
			chosenSamples.add(10);
			chosenSamples.add(11);
			chosenSamples.add(12);
			break;
		case 11:
			// C, D, Eb, E, F, F#, G, A, Bb, B, C'
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(3);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(6);
			chosenSamples.add(7);
			chosenSamples.add(9);
			chosenSamples.add(10);
			chosenSamples.add(11);
			chosenSamples.add(12);
			break;
		case 12:
			// C, C#, D, Eb, E, F, F#, G, A, Bb, B, C'
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(1);
			chosenSamples.add(2);
			chosenSamples.add(3);
			chosenSamples.add(4);
			chosenSamples.add(5);
			chosenSamples.add(6);
			chosenSamples.add(7);
			chosenSamples.add(9);
			chosenSamples.add(10);
			chosenSamples.add(11);
			chosenSamples.add(12);
			break;
		case 13:
			// Full chromatic scale
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
			chosenSamples.add(12);
			break;
		}

	}

	public static void doSound(int sndId) {
		float volume = 1;
		if (IntroActivity.loaded) {
			// first stop previous sound
			IntroActivity.soundPool.stop(isPlaying);
			// store the stream (thread) in which the new one is playing, to stop it in case
			isPlaying = IntroActivity.soundPool.play(chosenSamples.get(sndId) + 1, volume, volume, 1, 0, 1f);
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
		super.onPause();
		Log.i(SB_LifeCycle, "Game Activity On Pause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		// do nothing, because it's called even at opening HighScores
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
		savedInstanceState.putString("mode", getMode());
		// superclass method
		super.onSaveInstanceState(savedInstanceState);
	}

	public static String getMode() {
		return Mode;
	}

	public static void setMode(String mode) {
		Mode = mode;
	}

	public static String[] getModes() {
		return Modes;
	}

	public static void setModes(String[] modes) {
		Modes = modes;
	}

	public void delayNewHand() {
		Thread thread = new Thread() {
			@Override
			public void run() {

				// Block this thread for 2 seconds.
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}

				// After sleep finished blocking, create a Runnable to run on
				// the UI Thread.
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						drawView.startNew();
					}
				});
			}
		};

		// Don't forget to start the thread.
		thread.start();
	}

}
