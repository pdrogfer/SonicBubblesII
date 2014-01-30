/* this is the class for the bubbles, or dots. Implement
 * set and get position (posX, posY), on click, etc
 */

package pgf.sonicbubblesii;

import java.util.Random;

public class Dot {

	// native variables
	int posX, posY;
	int radius = 10;

	// constructors
	public Dot() {
		// allocate the dot somewhere within the canvas limits, and away from
		// the borders
		Random random = new Random();
		posX = random.nextInt((DrawingView.w - radius) + 1) + radius;
		posY = random.nextInt((DrawingView.h - radius) + 1) + radius;
	}

	// methods

	public void onDraw() {

	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

}
