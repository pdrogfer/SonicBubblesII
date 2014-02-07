package pgf.sonicbubblesii;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		drawView = (DrawingView)findViewById(R.id.drawing);
		listenAgain = (Button) findViewById(R.id.btnListenAgain);
		newGame = (Button) findViewById(R.id.btnNewGame);
		listenAgain.setOnClickListener(this);
		newGame.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
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

}
