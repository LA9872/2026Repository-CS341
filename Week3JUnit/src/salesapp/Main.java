package salesapp;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Controller: builds the window, creates the events, and instantiates a SalesSlip.
 */
public class Main extends JFrame {

    private static final long serialVersionUID = 1L;

    private final SalesSlip slip = new SalesSlip();

    private JPanel contentPane;
    private JTextField itemField;
    private JTextField costField;
    private JTextField quantityField;
    private JTextField totalField;
    private DefaultListModel<String> listModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        setTitle("Sales List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 560, 520);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel titleLabel = new JLabel("Sales List");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        titleLabel.setBounds(0, 10, 544, 40);
        contentPane.add(titleLabel);

        JLabel itemLabel = new JLabel("Item:");
        itemLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        itemLabel.setBounds(60, 70, 90, 25);
        contentPane.add(itemLabel);

        itemField = new JTextField();
        itemField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        itemField.setBounds(160, 68, 300, 28);
        contentPane.add(itemField);

        JLabel costLabel = new JLabel("Cost: $");
        costLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        costLabel.setBounds(60, 110, 90, 25);
        contentPane.add(costLabel);

        costField = new JTextField();
        costField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        costField.setBounds(160, 110, 150, 28);
        contentPane.add(costField);

        JLabel quantityLabel = new JLabel("Quantity");
        quantityLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        quantityLabel.setBounds(60, 150, 90, 25);
        contentPane.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        quantityField.setBounds(160, 150, 150, 28);
        contentPane.add(quantityField);

        JButton addButton = new JButton("Add Item to the Sales List");
        addButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        addButton.setBounds(60, 195, 400, 32);
        addButton.addActionListener(e -> addItem());
        contentPane.add(addButton);

        listModel = new DefaultListModel<>();
        JList<String> itemList = new JList<>(listModel);
        itemList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBounds(60, 240, 400, 150);
        contentPane.add(scrollPane);

        JLabel totalLabel = new JLabel("Total Sales:");
        totalLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        totalLabel.setBounds(60, 415, 110, 25);
        contentPane.add(totalLabel);

        totalField = new JTextField("$0.00");
        totalField.setEditable(false);
        totalField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        totalField.setBounds(180, 415, 150, 28);
        contentPane.add(totalField);
    }

    /** Reads the fields, adds the item to the SalesSlip, and refreshes the display. */
    private void addItem() {
        try {
            String name = itemField.getText();
            double cost = Double.parseDouble(costField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            SalesItem item = slip.addItem(name, cost, quantity);
            listModel.addElement(item.toString());
            totalField.setText(String.format("$%.2f", slip.computeTotal()));

            itemField.setText("");
            costField.setText("");
            quantityField.setText("");
            itemField.requestFocus();
        } catch (NumberFormatException ex) {
            showError("Cost must be a number and quantity must be a whole number.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Entry", JOptionPane.ERROR_MESSAGE);
    }
}