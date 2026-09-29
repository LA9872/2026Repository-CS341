package salesapptesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import salesapp.SalesItem;
import salesapp.SalesSlip;

class SalesSliptest {

    @Test
    void itemStoresValuesAndComputesLineTotal() {
        SalesItem item = new SalesItem("Choco Waffles", 10.45, 4);
        assertEquals("Choco Waffles", item.getName());
        assertEquals(10.45, item.getPrice(), 0.001);
        assertEquals(4, item.getQuantity());
        assertEquals(41.80, item.getLineTotal(), 0.001);
    }

    @Test
    void itemToStringContainsAllFields() {
        String s = new SalesItem("Sour Cream", 1.99, 1).toString();
        assertTrue(s.contains("Sour Cream"));
        assertTrue(s.contains("1.99"));
    }

    @Test
    void itemRejectsBadValues() {
        assertThrows(IllegalArgumentException.class, () -> new SalesItem("", 1.00, 1));
        assertThrows(IllegalArgumentException.class, () -> new SalesItem("A", 100.00, 1));
        assertThrows(IllegalArgumentException.class, () -> new SalesItem("A", 1.00, 0));
    }

    @Test
    void slipComputesTotalAcrossItems() {
        SalesSlip slip = new SalesSlip();
        slip.addItem("Choco Waffles", 10.45, 4);
        slip.addItem("Ginger Cookies", 2.00, 1);
        slip.addItem("Caramel Soda", 2.20, 1);
        assertEquals(3, slip.getItemCount());
        assertEquals(46.00, slip.computeTotal(), 0.001);
    }

    @Test
    void slipToStringHasOneLinePerItem() {
        SalesSlip slip = new SalesSlip();
        slip.addItem("Wheat Tortilla", 3.50, 1);
        slip.addItem("Sour Cream", 1.99, 1);
        assertEquals(2, slip.toString().split("\n").length);
    }
}