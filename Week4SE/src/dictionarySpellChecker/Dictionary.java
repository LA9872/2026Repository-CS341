package dictionarySpellChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.IdentityHashMap;

/** Dictionary backed by a hand-written Binary Search Tree. */
public class Dictionary {

    private static class Node {
        String word;
        Node left, right;
        Node(String word) { this.word = word; }
    }

    private Node root;
    private int size;
    private boolean removed; // set by recursive delete

    private static String normalize(String w) {
        return (w == null) ? null : w.trim().toLowerCase();
    }

    /** Inserts at a leaf. Returns false if word is empty or already present. */
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

    /** Removes a word. Handles: not found, leaf, one child, two children. */
    public boolean deleteWordNode(String word) {
        word = normalize(word);
        removed = false;
        if (word == null || word.isEmpty()) return false;
        root = delete(root, word);
        if (removed) size--;
        return removed;
    }

    private Node delete(Node n, String word) {
        if (n == null) return null;                    // (a) not in tree
        int cmp = word.compareTo(n.word);
        if (cmp < 0) {
            n.left = delete(n.left, word);
        } else if (cmp > 0) {
            n.right = delete(n.right, word);
        } else {
            removed = true;
            if (n.left == null) return n.right;        // (b) no children / (c) one child
            if (n.right == null) return n.left;        // (c)
            Node succ = n.right;                       // (d) two children: in-order successor
            while (succ.left != null) succ = succ.left;
            n.word = succ.word;
            boolean saved = removed;
            n.right = delete(n.right, succ.word);
            removed = saved;
        }
        return n;
    }

    /** Search helper: true if the word is in the tree. */
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

    /** True if the word is spelled correctly (found in the dictionary). */
    public boolean spellCheck(String word) {
        return checkWord(word);
    }

    /** Words in sorted (in-order) order. */
    public List<String> getSortedWords() {
        List<String> out = new ArrayList<>();
        inOrder(root, out);
        return out;
    }

    private void inOrder(Node n, List<String> out) {
        if (n == null) return;
        inOrder(n.left, out);
        out.add(n.word);
        inOrder(n.right, out);
    }

    public int size() { return size; }
    public boolean isEmpty() { return root == null; }

    /** Builds the dictionary from a paragraph (splits on non-letters). */
    public void buildFromText(String text) {
        for (String w : text.split("[^A-Za-z']+")) insertWordNode(w);
    }

    /**
     * Verifies BST properties: no cycles, every node within (lo, hi) bounds
     * (so left < parent < right), and node count matches size.
     */
    public boolean isValidBST() {
        Set<Node> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        return validate(root, null, null, seen) && seen.size() == size;
    }

    private boolean validate(Node n, String lo, String hi, Set<Node> seen) {
        if (n == null) return true;
        if (!seen.add(n)) return false;                // cycle / shared node
        if (lo != null && n.word.compareTo(lo) <= 0) return false;
        if (hi != null && n.word.compareTo(hi) >= 0) return false;
        return validate(n.left, lo, n.word, seen) && validate(n.right, n.word, hi, seen);
    }
}