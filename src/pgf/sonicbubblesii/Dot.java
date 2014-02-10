/* this is the class for the bubbles, or dots. Implement
 * set and get position (posX, posY), on click, etc
 */

package pgf.sonicbubblesii;

import java.util.Random;

import android.util.Log;

public class Dot {

	// native variables
	private final String SBDot = "Sonic Bubbles II-Dot";

	private int posX, posY;
	private int sampleInd; // index of the sample corresponding to the dot
	static int radius = DrawingView.width / 25; // TO make it relative to canvas
												// width
	Random random = new Random();

	// constructors
	public Dot() {
		// allocate the dot somewhere within the canvas limits, and away from
		// the borders

	}

	// methods

	public void onDraw() {

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

	/* That is: as the soundpool objects are represented by it's index (int),
	 * each new dot created will have a random int associated ("sampleInd"), in the range of the 
	 * actual game-level(represented here by the amount of possible sounds, "samplesSize") 
	 * used to fire the sample
	 */
	public void setSample(int samplesSize) {
		sampleInd = random.nextInt(samplesSize);
		//Log.i(SBDot, "This dot sample index is: " + sampleInd);
		
	}

	public int getSample() {
		return sampleInd;
	}
}
