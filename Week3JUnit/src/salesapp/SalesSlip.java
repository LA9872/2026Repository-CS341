package salesapp;

import java.util.ArrayList;

/**
 * Models the list of items being purchased.
 */
public class SalesSlip {
    private final ArrayList<SalesItem> items = new ArrayList<>();

    /** Creates a SalesItem, adds it to the list, and returns it. */
    public SalesItem addItem(String name, double price, int quantity) {
        SalesItem item = new SalesItem(name, price, quantity);
        items.add(item);
        return item;
    }

    public int getItemCount() {
        return items.size();
    }

    public SalesItem getItem(int index) {
        return items.get(index);
    }

    /** Total sales: sum of price * quantity for every item. */
    public double computeTotal() {
        double total = 0;
        for (SalesItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    /** One line per item, in the order added. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SalesItem item : items) {
            sb.append(item).append("\n");
        }
        return sb.toString();
    }
}