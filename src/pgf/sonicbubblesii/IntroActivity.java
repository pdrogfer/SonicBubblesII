package pgf.sonicbubblesii;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntroActivity extends Activity implements OnClickListener {

	Button startGame;

	// for sound
	public static SoundPool soundPool;
	private int soundC, soundCS, soundD, soundDS, soundE, soundF, soundFS, soundG, soundGS, soundA,
			soundAS, soundB;
	public static boolean loaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		startGame = (Button) findViewById(R.id.btnStartGame);
		startGame.setOnClickListener(this);
		
		soundSetup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.general_menu, menu);
		return true;
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
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
			break;
		}

	}

}
