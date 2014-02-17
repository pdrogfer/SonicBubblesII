package pgf.sonicbubblesii;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends Activity implements OnClickListener {

	// GUI
	Button listenAgain, newGame;
	DrawingView drawView;
	static Theme theme;


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

		// Number of Dots initialized here
		themeSetup(4, 7);
		levelSetup(theme.getNumSamples());
		
		
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
		Log.i(DrawingView.SB, "New Theme created"); 
		//theme.playTheme(1000);
		
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

	public static void messages(int msg, boolean bool) {
		if (msg == 1) {
			String text = bool ? "You got it" : "Nope! try again?";
			// Problems here!!!!
			// Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			//return toast;
			Log.i(DrawingView.SB, text);
		}
	}
	private void levelSetup(int levels) {
		/*
		 * Define the level to play in by setting the amount of sounds to use
		 * and wich ones. Create more levels: minor, pentatonic, blues, whole-tone, etc 
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
