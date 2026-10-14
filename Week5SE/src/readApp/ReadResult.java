package readApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds everything produced by reading an input file: the valid numbers,
 * plus a summary of lines that were skipped or rejected.
 * 
 * @author Leonardo A
 * @version 3.0
 */
public class ReadResult {

	private final NumberLinkedList numbers = new NumberLinkedList();
	private final List<String> invalidEntries = new ArrayList<>();
	private int blankLineCount;
	private int totalLines;

	public NumberLinkedList getNumbers() {
		return numbers;
	}

	/** @return descriptions such as "Line 3: \"3b4\"" for each rejected line */
	public List<String> getInvalidEntries() {
		return invalidEntries;
	}

	public int getInvalidCount() {
		return invalidEntries.size();
	}

	public int getBlankLineCount() {
		return blankLineCount;
	}

	public int getTotalLines() {
		return totalLines;
	}

	void addInvalid(int lineNumber, String text) {
		invalidEntries.add("Line " + lineNumber + ": \"" + text + "\"");
	}

	void incrementBlank() {
		blankLineCount++;
	}

	void incrementTotal() {
		totalLines++;
	}
}