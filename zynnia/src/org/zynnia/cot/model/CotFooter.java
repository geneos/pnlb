package org.zynnia.cot.model;

/**
 *
 * @author alejandro
 */
public class CotFooter {

    private final int totalShipments;

    /**
     * Constructor.
     *
     * @param totalShipments total shipments for send to ARBA.
     */
    public CotFooter(int totalShipments) {
        if (totalShipments < 0) {
            throw new IllegalArgumentException("Can't create CotFooter with negative shipments.");
        }
        this.totalShipments = totalShipments;
    }

    /**
     * Return the total number of shipments
     *
     * @return total number of shipments
     */
    public int getTotalShipments() {
        return totalShipments;
    }

    /**
     * Returns the total number of shipments.
     *
     * @return total number of shipments
     */
    @Override
    public String toString() {
        return String.valueOf(totalShipments);
    }
}
