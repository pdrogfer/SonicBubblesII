package pgf.sonicbubblesii;

public class Theme {

	// variables
	// game size, as the number of dots. Init = 4
	public int nDots;
	/* the level of difficulty, as the number of possible samples:
	 * Easy: 3, Medium 7, Difficult 13
	 */
	public int nSamples;

	// constructors
	public Theme(int nDots, int nSamples) {
		this.nDots = nDots;
		this.nSamples = nSamples;
	}

	// methods
	public int getNumDots() {
		return nDots;
	}

	public void setNumDots(int numDots) {
		this.nDots = numDots;
	}

	public int getNumSamples() {
		return nSamples;
	}

	public void setNumSamples(int numSamples) {
		this.nSamples = numSamples;
	}

	public void playTheme(final int loopDelayMillis, final int initDelayMillis) {
		// New thread to put this operation away from the main UI thread
		// loopDelayMillis = delay between sounds
		// initDelayMillis = initial delay before play first sound
		Thread thread = new Thread() {
			public void run() {
				android.os.SystemClock.sleep(initDelayMillis);
				for (int i = 0; i < DrawingView.dots.length; i++) {
					GameActivity.doSound(DrawingView.dots[i].getSample());
					android.os.SystemClock.sleep(loopDelayMillis);
				}
			}
		};
		thread.start();
	}
}
