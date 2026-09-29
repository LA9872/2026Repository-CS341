package salesapp;

/**
 * Models one item on the sales list: name, price, and quantity.
 */
public class SalesItem {
    private String name;
    private double price;
    private int quantity;

    public SalesItem(String name, double price, int quantity) {
        setName(name);
        setPrice(price);
        setQuantity(quantity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name is required.");
        }
        this.name = name.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0 || price >= 100) {
            throw new IllegalArgumentException("Cost must be at least $0 and less than $100.");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        this.quantity = quantity;
    }

    /** Calculation operation: price times quantity. */
    public double getLineTotal() {
        return price * quantity;
    }

    /** Name, price, and quantity in aligned columns (display with a monospaced font). */
    @Override
    public String toString() {
        return String.format("%-22s$%7.2f%6d", name, price, quantity);
    }
}