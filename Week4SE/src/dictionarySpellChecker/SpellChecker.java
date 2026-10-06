package dictionarySpellChecker;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Swing front end for the BST spell checker. The dictionary is built from
 * a sample paragraph; the user types text and each word is checked with
 * {@link Dictionary#spellCheck(String)}.
 */
public class SpellChecker {

    /** Paragraph whose words form the entire dictionary. */
    private static final String SAMPLE_PARAGRAPH =
        "Software engineering is the systematic application of engineering approaches "
        + "to the development of software. A binary search tree stores words in sorted "
        + "order so that searching for a word is fast. Every word in this paragraph is "
        + "spelled correctly and becomes part of the dictionary used by the spell checker.";

    /** Main window. */
    private JFrame frame;
    /** Text the user wants checked. */
    private JTextArea inputArea;
    /** Displays results and the sorted dictionary. */
    private JTextArea outputArea;
    /** The BST-backed dictionary. */
    private final Dictionary dictionary = new Dictionary();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SpellChecker window = new SpellChecker();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public SpellChecker() {
        dictionary.buildFromText(SAMPLE_PARAGRAPH);
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("BST Spell Checker");
        frame.setBounds(100, 100, 560, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(8, 8));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(4, 4));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel lblEnter = new JLabel("Enter text to spell check:");
        topPanel.add(lblEnter, BorderLayout.NORTH);

        inputArea = new JTextArea(5, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setText("Software engineering uses a binry search tree");
        topPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane outScroll = new JScrollPane(outputArea);
        outScroll.setBorder(BorderFactory.createTitledBorder("Results"));
        frame.getContentPane().add(outScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnCheck = new JButton("Check Spelling");
        btnCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkSpelling();
            }
        });
        buttonPanel.add(btnCheck);

        JButton btnShow = new JButton("Show Dictionary (sorted)");
        btnShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDictionary();
            }
        });
        buttonPanel.add(btnShow);
    }

    /** Checks each word in the input box and reports OK or MISSPELLED. */
    private void checkSpelling() {
        StringBuilder sb = new StringBuilder();
        int bad = 0;
        for (String w : inputArea.getText().split("[^A-Za-z']+")) {
            if (w.isEmpty()) continue;
            boolean ok = dictionary.spellCheck(w);
            if (!ok) bad++;
            sb.append(String.format("%-20s %s%n", w, ok ? "OK" : "MISSPELLED"));
        }
        sb.append(System.lineSeparator()).append(bad).append(" misspelled word(s).");
        outputArea.setText(sb.toString());
    }

    /** Displays all dictionary words in sorted order. */
    private void showDictionary() {
        StringBuilder sb = new StringBuilder("Dictionary (" + dictionary.size() + " words):\n");
        for (String w : dictionary.getSortedWords()) sb.append(w).append('\n');
        outputArea.setText(sb.toString());
        outputArea.setCaretPosition(0);
    }
}