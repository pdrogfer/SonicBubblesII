package pgf.sonicbubblesii;


public class Theme {

	// variables
	int numDots, numSamples;
	// game size, as the number of dots. Init = 4
	public static int nDots = 4;
	// the level of difficulty, as the number of possible samples. Init = 4
	public static int nSamples = 4;
	private static int i = 0;

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

	public void playTheme(int delayMillis) {
		for (int i = 0; i < DrawingView.dots.length; i++) {
			GameActivity.doSound(DrawingView.dots[i].getSample());
			android.os.SystemClock.sleep(1000);
		}
	}
}
