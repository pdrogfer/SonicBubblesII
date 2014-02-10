package pgf.sonicbubblesii;

import java.util.ArrayList;
import java.util.List;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameActivity extends Activity implements OnClickListener {

	// GUI
	Button listenAgain, newGame;
	DrawingView drawView;

	// for sound
	private static SoundPool soundPool;
	private int soundC, soundCS, soundD, soundDS, soundE, soundF, soundFS, soundG, soundGS, soundA,
			soundAS, soundB;
	static boolean loaded = false;
	// the level of difficulty, as the number of possible samples. Init = 4
	static int levels = 4;
	// an arraylist of int to define wich samples are used in each level
	static List<Integer> levelSamples = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		drawView = (DrawingView) findViewById(R.id.drawing);
		listenAgain = (Button) findViewById(R.id.btnListenAgain);
		newGame = (Button) findViewById(R.id.btnNewGame);
		listenAgain.setOnClickListener(this);
		newGame.setOnClickListener(this);

		soundSetup();
		levelSetup(levels);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
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
		case R.id.btnListenAgain:
			// play again the sequence
			break;
		case R.id.btnNewGame:
			// start a new game
			drawView.startNew();
			break;
		default:
			break;
		}

	}

	private void levelSetup(int levels) {
		/*
		 * Define the level to play in by setting the amount of sounds to use
		 * and wich ones
		 */
		switch (levels) {
		case 4:
			levelSamples.clear();
			levelSamples.add(0);
			levelSamples.add(2);
			levelSamples.add(4);
			levelSamples.add(7);
			break;

		case 5:
			levelSamples.clear();
			levelSamples.add(0);
			levelSamples.add(2);
			levelSamples.add(4);
			levelSamples.add(7);
			levelSamples.add(9);
			break;

		case 6:

			break;

		case 7:

			break;

		case 8:

			break;
		}

	}

	public static void doSound(int sndId) {
		float volume = 1;
		if (loaded) {
			soundPool.play(levelSamples.get(sndId) + 1, volume, volume, 1, 0, 1f);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		soundPool.release();
	}

}
