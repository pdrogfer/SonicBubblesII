package pgf.sonicbubblesii;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
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


	// an arraylist of int to define wich samples are used in each level
	static List<Integer> chosenSamples = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		drawView = (DrawingView) findViewById(R.id.drawing);
		listenAgain = (Button) findViewById(R.id.btnListenAgain);
		newGame = (Button) findViewById(R.id.btnNewGame);
		listenAgain.setOnClickListener(this);
		newGame.setOnClickListener(this);

		levelSetup(Theme.nSamples);
		themeSetup(4, 4);
		// theme.playTheme(2000);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}



	private void themeSetup(int nDots, int nSamples) {
		// generate a theme
		theme = new Theme(nDots, nSamples);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnListenAgain:
			// play again the sequence
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

	private void levelSetup(int levels) {
		/*
		 * Define the level to play in by setting the amount of sounds to use
		 * and wich ones
		 */
		switch (levels) {
		case 4:
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(7);
			break;

		case 5:
			chosenSamples.clear();
			chosenSamples.add(0);
			chosenSamples.add(2);
			chosenSamples.add(4);
			chosenSamples.add(7);
			chosenSamples.add(9);
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
			IntroActivity.soundPool.play(chosenSamples.get(sndId) + 1, volume, volume, 1, 0, 1f);
			Log.i("tag", "sampleTriggered");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		IntroActivity.soundPool.release();
	}

}
