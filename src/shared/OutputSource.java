package shared;

import java.io.File;
import java.io.IOException;

public class OutputSource {
	// set to Object for now because nature of output source is unknown
	Object outputSource;

	public OutputSource() {
		this(System.out);
	}

	public OutputSource(Object outputSource) {
		this.outputSource = outputSource;
	}

	// create output file if it doesn't exist
	public OutputSource(String outputFilePath) {
		if (outputFilePath == null) {
			throw new IllegalArgumentException("outputFilePath cannot be null");
		}
		this.outputSource = outputFilePath;
		File f = new File(outputFilePath);
		try {
			File parent = f.getParentFile();
			if (parent != null && !parent.exists()) {
				throw new IllegalStateException("Parent directory does not exist for output file: " + outputFilePath);
			}
			if (!f.exists()) {
				if (!f.createNewFile()) {
					throw new IllegalStateException("Could not create output file: " + outputFilePath);
				}
			} else if (f.isDirectory()) {
				throw new IllegalStateException("Output path is a directory: " + outputFilePath);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Failed to create output file: " + outputFilePath, e);
		}
	}

	public Object getOutputSource() {
		return outputSource;
	}

	public String getFilePath() {
		if (!(outputSource instanceof String)) {
			throw new IllegalStateException("Output source is not a file path");
		}
		return (String) outputSource;
	}
}