package org.zynnia.cot.utils;

import java.util.HashMap;
import org.zynnia.cot.interfaces.ICotGenerator;
import org.zynnia.cot.model.CotFooter;
import org.zynnia.cot.model.CotHeader;
import org.zynnia.cot.model.CotShipping;

/**
 *
 * @author Alejandro Scott
 */
public abstract class CotGenerator implements ICotGenerator {

    private final String user;

    private final String password;

    private String cotCode;

    private CotHeader header;

    private CotFooter footer;

    private HashMap<String, CotShipping> shipments;

    /**
     * Constructor.
     *
     * @param usr username registered in ARBA for the client
     * @param passwd user password registered with ARBA by the customer
     */
    public CotGenerator(String usr, String passwd) {
        this.user = usr;
        this.password = passwd;
        this.shipments = new HashMap<String, CotShipping>();
    }

    /**
     * Return the COT code generated by ARBA for shipping added.
     *
     * @return COT code generated
     */
    public String getCotCode() {
        generateCot(user, password, header, footer, shipments);
        return cotCode;
    }

    /**
     * Sets the header to be sent to ARBA.
     *
     * @param header the header with organizaton data
     */
    public void setHeader(CotHeader header) {
        this.header = header;
    }

    /**
     * Return the header of the data send to ARBA.
     *
     * @return header with organizaton data
     * @see CotHeader
     */
    public CotHeader getHeader() {
        return header;
    }

    /**
     * Sets the header to be sent to ARBA.
     *
     * @param footer the footer with products summary
     */
    public void setFooter(CotFooter footer) {
        this.footer = footer;
    }

    /**
     * Return the footer of the data send to ARBA.
     *
     * @return header with organizaton data
     * @see CotFooter
     */
    public CotFooter getFooter() {
        return footer;
    }

    /**
     * Add shipment to generator for send to ARBA.
     *
     * @param shipping the shipping to be sent to ARBA
     * @throws IllegalArgumentException if the shipping is null.
     * @see CotShipping
     */
    public void addShipping(CotShipping shipping) {
        if (shipping == null) {
            throw new IllegalArgumentException("Can't add null shipping to cot generator.");
        }
        shipments.put(shipping.getCode(), shipping);
    }


    /**
     * Generate the COT code.
     * This methos should be overwritted in the sub classes for generate the COT code,
     * either through HTTP or any other method you want to implement.
     *
     * @param user the user used for generate COT in ARBA.
     * @param passwd the user's password used for generate COT in ARBA.
     * @param header the value of header for send the shipments to ARBA.
     * @param footer the value of footer for send the shipments to ARBA.
     * @param shipments the value of shipments for send to ARBA.
     * @see CotHttpServiceGenerator
     */
    protected abstract String generateCot(String user, String passwd, CotHeader header, CotFooter footer, HashMap<String, CotShipping> shipments);


}
