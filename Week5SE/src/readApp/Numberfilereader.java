package readApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * Reads real numbers (one per line) from a text source into a linked list.
 *
 * Assumptions / decisions:
 * - Blank (or whitespace-only) lines are ignored and counted, not treated as errors.
 * - Leading/trailing whitespace around a number is allowed.
 * - Valid format: optional sign, digits with optional decimal point, optional
 *   exponent (e.g. 12, -3.5, .5, 1e3). Things Double.parseDouble would accept
 *   but that are not plain numbers ("NaN", "Infinity", "1f", "0x1p3") are invalid.
 * - Numbers too large for a double (overflow to infinity) are invalid.
 * 
 * @author Leonardo A
 * @version 3.0
 */
public class Numberfilereader {

	private static final Pattern NUMBER_PATTERN =
			Pattern.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?");

	/** Reads from a file on disk. */
	public static ReadResult read(File file) throws IOException {
		try (Reader reader = new FileReader(file)) {
			return read(reader);
		}
	}

	/** Reads from any Reader (also makes the logic easy to unit test). */
	public static ReadResult read(Reader source) throws IOException {
		ReadResult result = new ReadResult();
		BufferedReader reader = new BufferedReader(source);
		String line;
		int lineNumber = 0;

		while ((line = reader.readLine()) != null) {
			lineNumber++;
			result.incrementTotal();
			String trimmed = line.trim();

			if (trimmed.isEmpty()) {
				result.incrementBlank();
			} else if (NUMBER_PATTERN.matcher(trimmed).matches()) {
				double value = Double.parseDouble(trimmed);
				if (Double.isInfinite(value)) {
					result.addInvalid(lineNumber, trimmed);
				} else {
					result.getNumbers().add(value);
				}
			} else {
				result.addInvalid(lineNumber, trimmed);
			}
		}
		return result;
	}
}