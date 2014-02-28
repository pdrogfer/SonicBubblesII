package pgf.sonicbubblesii;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
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
		Log.i(SB_LifeCycle, "Intro Activity On Create");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle selection on general_menu items
		switch (item.getItemId()) {
		case R.id.new_game:
			// open a new game
			Intent intentNew = new Intent(this, GameActivity.class);
			startActivity(intentNew);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			return true;
		case R.id.best_scores:
			// show the best scores
			Intent intentBest = new Intent(this, HighScores.class);
			startActivity(intentBest);
			return true;
		case R.id.hot_to_play:
			// show activity_how
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
			// launch game activity
			Intent intent1 = new Intent(this, GameActivity.class);
			startActivity(intent1);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			break;
		case R.id.btnHowToPlay:
			// show instructions
			Intent intent2 = new Intent(this, HowToPlay.class);
			startActivity(intent2);
			// overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			break;
		}

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(SB_LifeCycle, "Intro Activity On Start");
		if (!IntroActivity.loaded) {
			
		}
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.i(SB_LifeCycle, "Intro Activity On Restart");
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(SB_LifeCycle, "Intro Activity On Resume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(SB_LifeCycle, "Intro Activity On Pause");
		super.onPause();
	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(SB_LifeCycle, "Intro Activity On Stop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// IntroActivity.soundPool.release();
		Log.i(SB_LifeCycle, "Intro Activity On Destroy");
	}

}
