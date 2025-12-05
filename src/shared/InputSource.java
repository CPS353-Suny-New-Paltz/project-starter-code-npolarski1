package shared;

import java.util.List;

public class InputSource {

    private String filePath;
    private List<Integer> manualInts;

    // No-arg constructor for placeholders
    public InputSource() {
        this.filePath = null;
        this.manualInts = null;
    }

    // Constructor for file-based input
    public InputSource(String filePath) {
        this.filePath = filePath;
        this.manualInts = null;
    }

    // Constructor for manual integer inputs
    public InputSource(List<Integer> manualInts) {
        this.manualInts = manualInts;
        this.filePath = null;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<Integer> getManualInts() {
        return manualInts;
    }
}