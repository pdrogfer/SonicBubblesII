/* this is the class for the bubbles, or dots. Implement
 * set and get position (posX, posY), on click, etc
 */

package pgf.sonicbubblesii;

import java.util.Random;

public class Dot {

	// native variables
	private int posX, posY;
	static int radius = DrawingView.width / 25; // TO make it relative to canvas width
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

}
