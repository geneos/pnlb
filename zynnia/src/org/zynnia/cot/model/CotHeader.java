package org.zynnia.cot.model;

/**
 *
 * @author alejandro
 */
public class CotHeader {

    private final String cuit;

    /**
     * Constructor.
     *
     * @param cuit of the enterprise
     */
    public CotHeader(String cuit) {
        if (cuit == null) {
            throw new IllegalArgumentException("Can't create CotHeader with null CUIT.");
        }
        this.cuit = cuit;
    }

    /**
     * Return the CUIT of enterprise
     *
     * @return CUIT code of enterprise
     */
    public String getCuit() {
        return cuit;
    }

    /**
     * Returns CUIT flattened version of the organization.
     * Only numbers no spaces and no hyphens (-)
     *
     * @return flattened version of CUIT
     */
    @Override
    public String toString() {
        return cuit.replaceAll("-", "");
    }
}
