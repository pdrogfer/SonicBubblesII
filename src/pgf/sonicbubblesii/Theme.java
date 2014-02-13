package pgf.sonicbubblesii;

public class Theme {

	// variables
	int numDots, numSamples;
	
	// constructors
	public Theme(int nDots, int nSamples) {
		this.numDots = nDots;
		this.numSamples = nSamples;
		
	}

	public int getNumDots() {
		return numDots;
	}

	public void setNumDots(int numDots) {
		this.numDots = numDots;
	}

	public int getNumSamples() {
		return numSamples;
	}

	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;
	}
	
	// methods
	
}
