package dictionarySpellChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * A dictionary of correctly spelled words, stored in a hand-written
 * Binary Search Tree (BST).
 * <p>
 * Words are normalized (trimmed and lower-cased) before being stored or
 * searched, so "Cat" and "cat" are the same word. The tree never contains
 * duplicates: for every node, all words in its left subtree are smaller
 * and all words in its right subtree are larger.
 * </p>
 *
 * @author Leonardo A
 * @version 2.0
 */
public class Dictionary {

    /** A single node in the binary search tree. */
    private static class Node {
        /** The word stored in this node. */
        String word;
        /** Subtree of words smaller than {@code word}. */
        Node left;
        /** Subtree of words larger than {@code word}. */
        Node right;

        /**
         * Creates a leaf node.
         *
         * @param word the word to store
         */
        Node(String word) {
            this.word = word;
        }
    }

    /** Root of the tree, or {@code null} when the dictionary is empty. */
    private Node root;

    /** Number of words currently stored. */
    private int size;

    /** Set by {@link #delete(Node, String)} to report whether a node was removed. */
    private boolean removed;

    /**
     * Normalizes a word for storage and comparison.
     *
     * @param w the raw word, may be {@code null}
     * @return the trimmed, lower-case word, or {@code null} if {@code w} is null
     */
    private static String normalize(String w) {
        return (w == null) ? null : w.trim().toLowerCase();
    }

    /**
     * Inserts a word into the tree at a leaf position. Duplicates are ignored.
     *
     * @param word the word to insert
     * @return {@code true} if the word was added; {@code false} if it was
     *         null, empty, or already in the dictionary
     */
    public boolean insertWordNode(String word) {
        word = normalize(word);
        if (word == null || word.isEmpty()) return false;
        if (root == null) {
            root = new Node(word);
            size++;
            return true;
        }
        Node cur = root;
        while (true) {
            int cmp = word.compareTo(cur.word);
            if (cmp == 0) return false;               // no duplicates
            if (cmp < 0) {
                if (cur.left == null) { cur.left = new Node(word); size++; return true; }
                cur = cur.left;
            } else {
                if (cur.right == null) { cur.right = new Node(word); size++; return true; }
                cur = cur.right;
            }
        }
    }

    /**
     * Removes a word from the tree, re-linking pointers as needed.
     * <p>
     * Handles four cases: (a) the word is not in the tree, (b) the node has
     * no children, (c) the node has one child, and (d) the node has two
     * children, in which case it is replaced by its in-order successor.
     * </p>
     *
     * @param word the word to remove
     * @return {@code true} if the word was found and removed
     */
    public boolean deleteWordNode(String word) {
        word = normalize(word);
        removed = false;
        if (word == null || word.isEmpty()) return false;
        root = delete(root, word);
        if (removed) size--;
        return removed;
    }

    /**
     * Recursive helper for {@link #deleteWordNode(String)}.
     *
     * @param n    root of the current subtree
     * @param word the normalized word to remove
     * @return the new root of the subtree after removal
     */
    private Node delete(Node n, String word) {
        if (n == null) return null;                    // (a) not in tree
        int cmp = word.compareTo(n.word);
        if (cmp < 0) {
            n.left = delete(n.left, word);
        } else if (cmp > 0) {
            n.right = delete(n.right, word);
        } else {
            removed = true;
            if (n.left == null) return n.right;        // (b) leaf / (c) one child
            if (n.right == null) return n.left;        // (c) one child
            Node succ = n.right;                       // (d) two children
            while (succ.left != null) succ = succ.left;
            n.word = succ.word;
            boolean saved = removed;
            n.right = delete(n.right, succ.word);
            removed = saved;
        }
        return n;
    }

    /**
     * Searches the tree for a word.
     *
     * @param word the word to look for
     * @return {@code true} if the word is in the tree
     */
    public boolean checkWord(String word) {
        word = normalize(word);
        if (word == null || word.isEmpty()) return false;
        Node cur = root;
        while (cur != null) {
            int cmp = word.compareTo(cur.word);
            if (cmp == 0) return true;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return false;
    }

    /**
     * Checks whether a word is spelled correctly, meaning it is recognized
     * in the dictionary.
     *
     * @param word the word to check
     * @return {@code true} if the word is in the dictionary, {@code false}
     *         if it is misspelled, empty, or null
     */
    public boolean spellCheck(String word) {
        return checkWord(word);
    }

    /**
     * Returns every word in sorted (in-order) order.
     *
     * @return a new list of words in ascending alphabetical order
     */
    public List<String> getSortedWords() {
        List<String> out = new ArrayList<>();
        inOrder(root, out);
        return out;
    }

    /**
     * In-order traversal helper for {@link #getSortedWords()}.
     *
     * @param n   root of the current subtree
     * @param out list that receives the words
     */
    private void inOrder(Node n, List<String> out) {
        if (n == null) return;
        inOrder(n.left, out);
        out.add(n.word);
        inOrder(n.right, out);
    }

    /**
     * Returns the number of words in the dictionary.
     *
     * @return the word count
     */
    public int size() {
        return size;
    }

    /**
     * Tells whether the dictionary has no words.
     *
     * @return {@code true} if the tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Builds the dictionary from a block of text. The text is split on any
     * character that is not a letter or apostrophe, and each word is inserted.
     *
     * @param text a paragraph whose words are all assumed to be spelled correctly
     */
    public void buildFromText(String text) {
        for (String w : text.split("[^A-Za-z']+")) insertWordNode(w);
    }

    /**
     * Verifies the BST properties, intended for use in test assertions.
     * Checks that there are no cycles or shared nodes, that every node lies
     * strictly between the bounds set by all of its ancestors (so left
     * &lt; parent &lt; right), and that the number of nodes equals {@link #size()}.
     *
     * @return {@code true} if the tree is a valid BST
     */
    public boolean isValidBST() {
        Set<Node> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        return validate(root, null, null, seen) && seen.size() == size;
    }

    /**
     * Recursive helper for {@link #isValidBST()}.
     *
     * @param n    current node
     * @param lo   exclusive lower bound, or {@code null} for none
     * @param hi   exclusive upper bound, or {@code null} for none
     * @param seen nodes already visited, used to detect cycles
     * @return {@code true} if the subtree rooted at {@code n} is valid
     */
    private boolean validate(Node n, String lo, String hi, Set<Node> seen) {
        if (n == null) return true;
        if (!seen.add(n)) return false;                // cycle / shared node
        if (lo != null && n.word.compareTo(lo) <= 0) return false;
        if (hi != null && n.word.compareTo(hi) >= 0) return false;
        return validate(n.left, lo, n.word, seen) && validate(n.right, n.word, hi, seen);
    }
}