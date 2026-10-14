package readApptesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

import readApp.Numberfilereader;
import readApp.NumberLinkedList;
import readApp.ReadResult;

/** JUnit 5 tests for the linked list, file reader, and statistics. */
class Readapputiltest {

	private static final double DELTA = 1e-9;

	private ReadResult parse(String text) throws IOException {
		return Numberfilereader.read(new StringReader(text));
	}

	// ---- Required test conditions ----

	@Test
	void invalidInputIsReportedAndSkipped() throws IOException {
		ReadResult r = parse("1\n3b4\n5\n");
		assertEquals(2, r.getNumbers().size());
		assertEquals(1, r.getInvalidCount());
		assertTrue(r.getInvalidEntries().get(0).contains("3b4"));
		assertEquals(3.0, r.getNumbers().mean(), DELTA);
	}

	@Test
	void emptyFile() throws IOException {
		ReadResult r = parse("");
		assertTrue(r.getNumbers().isEmpty());
		assertEquals(0, r.getInvalidCount());
		assertTrue(Double.isNaN(r.getNumbers().mean()));
		assertTrue(Double.isNaN(r.getNumbers().sampleStandardDeviation()));
	}

	@Test
	void singleLineFile() throws IOException {
		ReadResult r = parse("42.5");
		assertEquals(1, r.getNumbers().size());
		assertEquals(42.5, r.getNumbers().mean(), DELTA);
		assertTrue(Double.isNaN(r.getNumbers().sampleStandardDeviation()));
	}

	@Test
	void emptyLinesInMiddleAreSkipped() throws IOException {
		ReadResult r = parse("2\n\n4\n   \n6\n");
		assertEquals(3, r.getNumbers().size());
		assertEquals(2, r.getBlankLineCount());
		assertEquals(0, r.getInvalidCount());
		assertEquals(4.0, r.getNumbers().mean(), DELTA);
		assertEquals(2.0, r.getNumbers().sampleStandardDeviation(), DELTA);
	}

	// ---- Additional tests ----

	@Test
	void knownMeanAndStandardDeviation() throws IOException {
		ReadResult r = parse("2\n4\n4\n4\n5\n5\n7\n9\n");
		assertEquals(5.0, r.getNumbers().mean(), DELTA);
		assertEquals(Math.sqrt(32.0 / 7.0), r.getNumbers().sampleStandardDeviation(), DELTA);
	}

	@Test
	void negativeDecimalAndExponentValues() throws IOException {
		ReadResult r = parse("-1.5\n.5\n+2\n1e1\n");
		assertEquals(4, r.getNumbers().size());
		assertEquals(0, r.getInvalidCount());
		assertEquals(11.0 / 4.0, r.getNumbers().mean(), DELTA);
	}

	@Test
	void nonNumericTokensAreInvalid() throws IOException {
		ReadResult r = parse("NaN\nInfinity\n1f\n0x10\n1,5\nabc\n1e999\n");
		assertEquals(0, r.getNumbers().size());
		assertEquals(7, r.getInvalidCount());
	}

	@Test
	void linkedListPreservesInsertionOrder() {
		NumberLinkedList list = new NumberLinkedList();
		list.add(3);
		list.add(1);
		list.add(2);
		double[] expected = { 3, 1, 2 };
		int i = 0;
		for (double v : list) {
			assertEquals(expected[i++], v, DELTA);
		}
		assertEquals(3, i);
		assertEquals(6.0, list.getSum(), DELTA);
	}

	@Test
	void readsFromRealFile() throws IOException {
		File f = File.createTempFile("numbers", ".txt");
		f.deleteOnExit();
		Files.writeString(f.toPath(), "10\n20\n30\n");
		ReadResult r = Numberfilereader.read(f);
		assertEquals(20.0, r.getNumbers().mean(), DELTA);
		assertEquals(10.0, r.getNumbers().sampleStandardDeviation(), DELTA);
	}
}