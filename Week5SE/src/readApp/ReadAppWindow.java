package readApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main GUI window. The user clicks a button, picks a text file with a
 * JFileChooser, and the mean and standard deviation of the numbers in the file
 * are displayed in a text area, along with a summary of any invalid input.
 * 
 * @author Leonardo A
 * @version 3.0
 */
public class ReadAppWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JButton btnSelectFile;
	private JLabel lblFile;
	private JTextArea textAreaResults;

	/** Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReadAppWindow frame = new ReadAppWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** Create the frame. */
	public ReadAppWindow() {
		setTitle("Mean and Standard Deviation Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 420);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(10, 0));
		contentPane.add(topPanel, BorderLayout.NORTH);

		btnSelectFile = new JButton("Select File...");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseAndProcessFile();
			}
		});
		topPanel.add(btnSelectFile, BorderLayout.WEST);

		lblFile = new JLabel("No file selected");
		topPanel.add(lblFile, BorderLayout.CENTER);

		textAreaResults = new JTextArea();
		textAreaResults.setEditable(false);
		textAreaResults.setFont(new Font("Monospaced", Font.PLAIN, 13));
		JScrollPane scrollPane = new JScrollPane(textAreaResults);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	}

	/** Opens a JFileChooser and processes the chosen file. */
	private void chooseAndProcessFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select a text file of numbers");
		chooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			lblFile.setText(file.getName());
			processFile(file);
		}
	}

	/** Reads the file, computes the statistics, and shows the report. */
	private void processFile(File file) {
		try {
			ReadResult result = Numberfilereader.read(file);
			textAreaResults.setText(buildReport(file, result));
			textAreaResults.setCaretPosition(0);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this,
					"Could not read file:\n" + ex.getMessage(),
					"File Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Builds the text shown in the results area. */
	static String buildReport(File file, ReadResult result) {
		NumberLinkedList numbers = result.getNumbers();
		StringBuilder sb = new StringBuilder();

		sb.append("File: ").append(file.getName()).append("\n");
		sb.append("Lines read:          ").append(result.getTotalLines()).append("\n");
		sb.append("Valid numbers:       ").append(numbers.size()).append("\n");
		sb.append("Blank lines skipped: ").append(result.getBlankLineCount()).append("\n");
		sb.append("Invalid lines:       ").append(result.getInvalidCount()).append("\n\n");

		if (numbers.isEmpty()) {
			sb.append("No valid numbers found, so no statistics can be calculated.\n");
		} else {
			sb.append(String.format("Mean:               %.4f%n", numbers.mean()));
			if (numbers.size() < 2) {
				sb.append("Standard deviation: undefined (needs at least 2 values)\n");
			} else {
				sb.append(String.format("Standard deviation: %.4f (sample, n-1)%n",
						numbers.sampleStandardDeviation()));
			}
		}

		if (result.getInvalidCount() > 0) {
			sb.append("\nInvalid input (ignored in calculations):\n");
			for (String entry : result.getInvalidEntries()) {
				sb.append("  ").append(entry).append("\n");
			}
		}
		return sb.toString();
	}
}
