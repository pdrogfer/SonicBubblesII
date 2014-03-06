package pgf.sonicbubblesii;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class IntroActivity extends Activity implements OnClickListener {

	Button startGame, howToPlay;

	// for sound
	public static SoundPool soundPool;
	private int soundC, soundCS, soundD, soundDS, soundE, soundF, soundFS, soundG, soundGS, soundA,
			soundAS, soundB;
	public static boolean loaded = false;

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
		startGame.setOnClickListener(this);
		howToPlay.setOnClickListener(this);

		soundSetup();
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
			newGame();
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartGame:
			newGame();
			break;
		case R.id.btnHowToPlay:
			instructions();
			break;
		}

	}

	public void newGame() {
		// choose level and open a new game
	
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(this);
		levelDialog.setTitle(R.string.dialog_level_title);
		levelDialog.setItems(R.array.string_array_levels, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO manage level choice
				switch (which) {
				case 0:
					Toast choice0 = Toast.makeText(getApplicationContext(), "choice Relax", Toast.LENGTH_SHORT);
					choice0.show();
					break;
				case 1:
					Toast choice1 = Toast.makeText(getApplicationContext(), "choice Standard", Toast.LENGTH_SHORT);
					choice1.show();
					break;
				case 2:
					Toast choice2 = Toast.makeText(getApplicationContext(), "choice Expert", Toast.LENGTH_SHORT);
					choice2.show();
					break;

				default:
					break;
				}
			}
		});
		levelDialog.show();
		
		Intent intentNew = new Intent(this, GameActivity.class);
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
