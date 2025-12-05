package shared;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputSource {
    private String filePath;

    public OutputSource() {
        this.filePath = null;
    }

    public OutputSource(String outputFilePath) {
        if (outputFilePath == null) {
            System.err.println("Output file path cannot be null");
            this.filePath = null;
            return;
        }
        this.filePath = outputFilePath;
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
            } else {
                // file exists and is a regular file - wipe it so we don't append to previous runs
                try (FileWriter fw = new FileWriter(f, false)) {
                    // opening FileWriter with append=false wipes the file
                } catch (IOException ioe) {
                    System.err.println("Failed to truncate existing output file: " + ioe.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to create/wipe output file: " + outputFilePath);
            return;
        }
    }

    public String getFilePath() {
        return filePath;
    }
}