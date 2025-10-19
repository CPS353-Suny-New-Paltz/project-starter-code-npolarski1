package shared;

public class InputSource {
	
	// set to Object for now because nature of input source is unknown
	Object inputSource;
	
	public InputSource(Object inputSource) {
		this.inputSource = inputSource;
	}
	
	public Object getInputSource() {
		return inputSource;
	}

	public String getFilePath() {
		// assuming input source is a file path represented as a String
		return (String) inputSource;
	}
}
