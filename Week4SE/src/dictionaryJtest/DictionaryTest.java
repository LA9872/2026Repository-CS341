package dictionaryJtest;

import dictionarySpellChecker.Dictionary;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DictionaryTest {

    private Dictionary d;

    @BeforeEach
    void setUp() { d = new Dictionary(); }

    private void load(String... words) { for (String w : words) d.insertWordNode(w); }

    // ---------- insertWordNode ----------
    @Test
    void insertIntoEmptyTree() {
        assertTrue(d.insertWordNode("mango"));
        assertEquals(1, d.size());
        assertTrue(d.checkWord("mango"));
        assertTrue(d.isValidBST());
    }

    @Test
    void insertLeftAndRightChildren() {
        load("mango", "apple", "zebra");
        assertEquals(Arrays.asList("apple", "mango", "zebra"), d.getSortedWords());
        assertEquals(3, d.size());
        assertTrue(d.isValidBST());
    }

    @Test
    void duplicatesRejected() {
        assertTrue(d.insertWordNode("cat"));
        assertFalse(d.insertWordNode("cat"));
        assertFalse(d.insertWordNode("CAT"));
        assertEquals(1, d.size());
        assertTrue(d.isValidBST());
    }

    @Test
    void invalidInputRejected() {
        assertFalse(d.insertWordNode(null));
        assertFalse(d.insertWordNode("   "));
        assertEquals(0, d.size());
    }

    @Test
    void sortedInsertionDegeneratesButStaysValid() {
        load("a", "b", "c", "d", "e", "f");
        assertEquals(Arrays.asList("a", "b", "c", "d", "e", "f"), d.getSortedWords());
        assertTrue(d.isValidBST());
    }

    @Test
    void insertKeepsAllPreviousWords() {
        String[] words = {"m", "f", "t", "b", "h", "p", "w", "a", "z"};
        for (int i = 0; i < words.length; i++) {
            d.insertWordNode(words[i]);
            assertEquals(i + 1, d.size());
            assertTrue(d.isValidBST(), "invalid after inserting " + words[i]);
            for (int j = 0; j <= i; j++) assertTrue(d.checkWord(words[j]));
        }
    }

    // ---------- deleteWordNode ----------
    @Test
    void deleteNotInTree() {
        load("b", "a", "c");
        assertFalse(d.deleteWordNode("zzz"));
        assertEquals(3, d.size());
        assertTrue(d.isValidBST());
    }

    @Test
    void deleteFromEmptyTree() {
        assertFalse(d.deleteWordNode("a"));
        assertEquals(0, d.size());
    }

    @Test
    void deleteLeaf() {
        load("m", "f", "t");
        assertTrue(d.deleteWordNode("f"));
        assertFalse(d.checkWord("f"));
        assertEquals(Arrays.asList("m", "t"), d.getSortedWords());
        assertEquals(2, d.size());
        assertTrue(d.isValidBST());
    }

    @Test
    void deleteNodeWithOneChild() {
        load("m", "f", "b");               // f has only left child b
        assertTrue(d.deleteWordNode("f"));
        assertEquals(Arrays.asList("b", "m"), d.getSortedWords());
        assertTrue(d.isValidBST());

        d = new Dictionary();
        load("m", "t", "w");               // t has only right child w
        assertTrue(d.deleteWordNode("t"));
        assertEquals(Arrays.asList("m", "w"), d.getSortedWords());
        assertTrue(d.isValidBST());
    }

    @Test
    void deleteNodeWithTwoChildren() {
        load("m", "f", "t", "b", "h", "p", "w");
        assertTrue(d.deleteWordNode("t"));
        assertEquals(Arrays.asList("b", "f", "h", "m", "p", "w"), d.getSortedWords());
        assertEquals(6, d.size());
        assertTrue(d.isValidBST());
    }

    @Test
    void deleteRootAllCases() {
        load("m", "f", "t");
        assertTrue(d.deleteWordNode("m"));
        assertEquals(Arrays.asList("f", "t"), d.getSortedWords());
        assertTrue(d.isValidBST());
        assertTrue(d.deleteWordNode("f"));
        assertTrue(d.deleteWordNode("t"));
        assertTrue(d.isEmpty());
        assertEquals(0, d.size());
        assertTrue(d.isValidBST());
    }

    // ---------- checkWord / spellCheck ----------
    @Test
    void spellCheckCorrectAndMisspelled() {
        d.buildFromText("The quick brown fox jumps over the lazy dog.");
        assertTrue(d.spellCheck("fox"));
        assertTrue(d.spellCheck("THE"));
        assertFalse(d.spellCheck("quikc"));
        assertFalse(d.spellCheck(""));
        assertFalse(d.spellCheck(null));
    }

    @Test
    void spellCheckSampleString() {
        d.buildFromText("the quick brown fox jumps over the lazy dog");
        String[] sample = "the quick brown fox jumpd over the lazy dog".split(" ");
        int wrong = 0;
        for (String w : sample) if (!d.spellCheck(w)) wrong++;
        assertEquals(1, wrong);
        assertFalse(d.spellCheck("jumpd"));
    }

    // ---------- sorted display / build ----------
    @Test
    void buildFromTextIsSortedWithoutDuplicates() {
        d.buildFromText("the cat and the hat and the bat");
        List<String> words = d.getSortedWords();
        assertEquals(Arrays.asList("and", "bat", "cat", "hat", "the"), words);
        assertTrue(d.isValidBST());
    }

    @Test
    void emptyTreeIsValid() {
        assertTrue(d.isValidBST());
        assertTrue(d.getSortedWords().isEmpty());
    }
}