package pgf.sonicbubblesii;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class IntroActivity extends Activity implements OnClickListener {

	TextView title, version;
	Button startGame, howToPlay, bestScores;
	Typeface tf_thin, tf_reg;

	// for sound
	public static SoundPool soundPool;
	private int soundC, soundCS, soundD, soundDS, soundE, soundF, soundFS, soundG, soundGS, soundA,
			soundAS, soundB, soundCC; // last one is C octave up
	public static boolean loaded = false;

	// options in creating a new game
	int numBubbles, numSounds, score, numLive, level, round, hand;
	private String[] Modes;
	private String Mode;

	// Log tags
	public final static String SB = "Sonic Bubbles II";
	public final static String SB_LifeCycle = "SB II LifeCycle";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		Log.i(SB_LifeCycle, "Intro Activity On Create");

		startGame = (Button) findViewById(R.id.btnStartGame);
		howToPlay = (Button) findViewById(R.id.btnHowToPlay);
		bestScores = (Button) findViewById(R.id.btnHighScores);
		title = (TextView) findViewById(R.id.tVwTitle);
		version = (TextView) findViewById(R.id.tVwVersion);
		startGame.setOnClickListener(this);
		howToPlay.setOnClickListener(this);
		bestScores.setOnClickListener(this);
		Modes = getResources().getStringArray(R.array.string_array_levels);
		// set font
		Typeface tf_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		Typeface tf_reg = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		title.setTypeface(tf_reg);
		version.setTypeface(tf_thin);
		startGame.setTypeface(tf_thin);
		howToPlay.setTypeface(tf_thin);
		bestScores.setTypeface(tf_thin);

		if (soundPool == null) {
			//to prevent loading unneccesary loads when returning from finished game
			soundSetup();
		}
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
			// chooseLevel calls newGame
			chooseLevel();
			return true;
		case R.id.best_scores:
			bestScores();
			return true;
		case R.id.hot_to_play:
			instructions();
			return true;
		case R.id.exit:
			exitAll();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void soundSetup() {
		// Set the hardware buttons to control the app volume
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sounds
		soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded = true;
			}
		});
		// the load function returns an int, the index of the loaded sound
		soundC = soundPool.load(this, R.raw.c, 1);
		soundCS = soundPool.load(this, R.raw.c_s, 1);
		soundD = soundPool.load(this, R.raw.d, 1);
		soundDS = soundPool.load(this, R.raw.d_s, 1);
		soundE = soundPool.load(this, R.raw.e, 1);
		soundF = soundPool.load(this, R.raw.f, 1);
		soundFS = soundPool.load(this, R.raw.f_s, 1);
		soundG = soundPool.load(this, R.raw.g, 1);
		soundGS = soundPool.load(this, R.raw.g_s, 1);
		soundA = soundPool.load(this, R.raw.a, 1);
		soundAS = soundPool.load(this, R.raw.a_s, 1);
		soundB = soundPool.load(this, R.raw.b, 1);
		soundCC = soundPool.load(this, R.raw.cc, 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartGame:
			// chooseLevel calls newGame
			chooseLevel();
			break;
		case R.id.btnHowToPlay:
			instructions();
			break;
		case R.id.btnHighScores:
			bestScores();
		}

	}

	public void chooseLevel() {
		// TODO this is written twice, here and in GameActivity. Maybe it can be
		// encapsulated somewhere else?
		// choose level and set variables for the new game
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(this);
		levelDialog.setTitle(R.string.dialog_level_title);
		levelDialog.setItems(R.array.string_array_levels, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// manage level choice
				switch (which) {
				case 0:
					numBubbles = 4;
					numSounds = 4;
					break;
				case 1:
					numBubbles = 4;
					numSounds = 7;
					break;
				case 2:
					numBubbles = 4;
					numSounds = 12;
					break;
				default:
					break;
				}
				score = 0;
				numLive = 5;
				level = 1;
				round = 1;
				hand = 1;
				Mode = Modes[which];
				newGame();
			}
		});
		levelDialog.show();
	}

	public void newGame() {
		// open a new game and pass default params
		Intent intentNew = new Intent(this, GameActivity.class);
		intentNew.putExtra("nBubbles", numBubbles);
		intentNew.putExtra("nSounds", numSounds);
		intentNew.putExtra("score", score);
		intentNew.putExtra("nLive", numLive);
		intentNew.putExtra("level", level);
		intentNew.putExtra("round", round);
		intentNew.putExtra("hand", hand);
		intentNew.putExtra("mode", Mode);
		startActivity(intentNew);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public void bestScores() {
		// show the best scores
		Intent intentBest = new Intent(this, HighScores.class);
		startActivity(intentBest);
	}

	public void instructions() {
		// show activity_how
		Intent intentHow = new Intent(this, HowToPlay.class);
		startActivity(intentHow);
	}

	public void exitAll() {
		// exit the app
		Intent intentExit = new Intent(Intent.ACTION_MAIN);
		intentExit.addCategory(Intent.CATEGORY_HOME);
		intentExit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intentExit);
	}

	@Override
	protected void onStart() {
		Log.i(SB_LifeCycle, "Intro Activity On Start");
		if (!IntroActivity.loaded) {

		}
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.i(SB_LifeCycle, "Intro Activity On Restart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(SB_LifeCycle, "Intro Activity On Resume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i(SB_LifeCycle, "Intro Activity On Pause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i(SB_LifeCycle, "Intro Activity On Stop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(SB_LifeCycle, "Intro Activity On Destroy");
	}

}
