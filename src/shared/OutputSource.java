package shared;

public class OutputSource {
	// set to Object for now because nature of output source is unknown
	Object outputSource;

	public OutputSource() {
		this(System.out);
	}

	public OutputSource(Object outputSource) {
		this.outputSource = outputSource;
	}

	public Object getOutputSource() {
		return outputSource;
	}
}
