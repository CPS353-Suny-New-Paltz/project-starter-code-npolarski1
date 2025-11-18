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
			System.err.println("Output file path cannot be null");
			return;
		}
		this.outputSource = outputFilePath;
		File f = new File(outputFilePath);
		try {
			File parent = f.getParentFile();
			if (parent != null && !parent.exists()) {
				System.err.println("Parent directory does not exist for output file: " + outputFilePath);
				return;
			}
			if (!f.exists()) {
				if (!f.createNewFile()) {
					System.err.println("Could not create output file: " + outputFilePath);
				}
			} else if (f.isDirectory()) {
				System.err.println("Output path is a directory: " + outputFilePath);
			}
		} catch (IOException e) {
			System.err.println("Failed to create output file: " + outputFilePath);
			return;
		}
	}

	public Object getOutputSource() {
		return outputSource;
	}

	public String getFilePath() {
		if (!(outputSource instanceof String)) {
			System.err.println("Output source is not a file path");
			return null;
		}
		return (String) outputSource;
	}
}