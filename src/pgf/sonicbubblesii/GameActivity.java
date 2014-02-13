package pgf.sonicbubblesii;

import java.util.ArrayList;
import java.util.List;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameActivity extends Activity implements OnClickListener {

	// GUI
	Button listenAgain, newGame;
	DrawingView drawView;
	Theme theme;


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

		levelSetup(levels);
		themeSetup();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}



	private void themeSetup() {
		// generate a theme
		theme = new Theme(2, 3);
		
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
		if (IntroActivity.loaded) {
			IntroActivity.soundPool.play(levelSamples.get(sndId) + 1, volume, volume, 1, 0, 1f);
			Log.i("tag", "sampleTriggered");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		IntroActivity.soundPool.release();
	}

}
