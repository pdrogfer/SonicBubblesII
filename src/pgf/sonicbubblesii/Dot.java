/* this is the class for the bubbles, or dots. Implement
 * set and get position (posX, posY), on click, etc
 */

package pgf.sonicbubblesii;

import java.util.Random;

import android.graphics.Color;
import android.util.Log;

public class Dot {

	// native variables
	private final String SBDot = "Sonic Bubbles II-Dot";

	private boolean trig;
	private int posX, posY, colorDot;
	private int sampleInd; // index of the sample corresponding to the dot
	static int radius = DrawingView.width / 25; // TO make it relative to canvas
												// width
	Random random = new Random();

	// constructors
	public Dot() {

	}

	// methods
	public int getColor() {
		return colorDot;
	}

	public void setColor() {
		colorDot = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX() {
		posX = random.nextInt(DrawingView.width);
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY() {
		posY = random.nextInt(DrawingView.height);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	/*
	 * That is: as the soundpool objects are represented by it's index (int),
	 * each new dot created will have a random int associated ("sampleInd"), in
	 * the range of the actual game-level(represented here by the amount of
	 * possible sounds, "samplesSize") used to fire the sample
	 */
	public void setSample(int samplesSize) {
		sampleInd = random.nextInt(samplesSize);
		// Log.i(SBDot, "This dot sample index is: " + sampleInd);

	}

	public int getSample() {
		return sampleInd;
	}

	public void setSampleTriggered(boolean trig) {
		/*
		 * returns true if sample has been played in current hand (hand =
		 * gesture between finger down and finger up)
		 */
		this.trig = trig;
	}

	public boolean getSampleTriggered() {
		return trig;
	}

}
