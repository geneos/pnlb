package org.zynnia.cot.model;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author alejandro
 */
public class CotShipping {

    private final Date date;

    private final String code;

    private final Date releaseDate;

    private final String subject;

    private final boolean targetFinalConsumer;

    private final boolean targetHolder;

    private final String targetStreetAddress;

    private final String targetNumberAddress;

    private final String targetPostalCodeAddress;

    private final String targetCityAddress;

    private final String targetProvinceAddress;

    private final boolean deliveryHomeAddress;

    private final String sourceCUIT;

    private final String sourceSocialReason;

    private final boolean transmitterHolder;

    private final String sourceStreetAddress;

    private final String sourceNumberAddress;

    private final String sourcePostalCodeAddress;

    private final String sourceCityAddress;

    private final String sourceProvinceAddress;

    private final String shipperCUIT;

    private final long total;

    private final HashMap<String, CotProduct> products;

    public CotShipping(Date date, String code, Date releaseDate, String subject, boolean targetFinalConsumer, boolean targetHolder, String targetStreetAddress, String targetNumberAddress, String targetPostalCodeAddress, String targetCityAddress, String targetProvinceAddress, boolean deliveryHomeAddress, String sourceCUIT, String sourceSocialReason, boolean transmitterHolder, String sourceStreetAddress, String sourceNumberAddress, String sourcePostalCodeAddress, String sourceCityAddress, String sourceProvinceAddress, String shipperCUIT, long total) {
        this.date = date;
        this.code = code;
        this.releaseDate = releaseDate;
        this.subject = subject;
        this.targetFinalConsumer = targetFinalConsumer;
        this.targetHolder = targetHolder;
        this.targetStreetAddress = targetStreetAddress;
        this.targetNumberAddress = targetNumberAddress;
        this.targetPostalCodeAddress = targetPostalCodeAddress;
        this.targetCityAddress = targetCityAddress;
        this.targetProvinceAddress = targetProvinceAddress;
        this.deliveryHomeAddress = deliveryHomeAddress;
        this.sourceCUIT = sourceCUIT;
        this.sourceSocialReason = sourceSocialReason;
        this.transmitterHolder = transmitterHolder;
        this.sourceStreetAddress = sourceStreetAddress;
        this.sourceNumberAddress = sourceNumberAddress;
        this.sourcePostalCodeAddress = sourcePostalCodeAddress;
        this.sourceCityAddress = sourceCityAddress;
        this.sourceProvinceAddress = sourceProvinceAddress;
        this.shipperCUIT = shipperCUIT;
        this.total = total;
        this.products = new HashMap<String, CotProduct>();
    }

    public String getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

    public boolean isDeliveryHomeAddress() {
        return deliveryHomeAddress;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getShipperCUIT() {
        return shipperCUIT;
    }

    public String getSourceCUIT() {
        return sourceCUIT;
    }

    public String getSourceCityAddress() {
        return sourceCityAddress;
    }

    public String getSourceNumberAddress() {
        return sourceNumberAddress;
    }

    public String getSourcePostalCodeAddress() {
        return sourcePostalCodeAddress;
    }

    public String getSourceProvinceAddress() {
        return sourceProvinceAddress;
    }

    public String getSourceSocialReason() {
        return sourceSocialReason;
    }

    public String getSourceStreetAddress() {
        return sourceStreetAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getTargetCityAddress() {
        return targetCityAddress;
    }

    public boolean isTargetFinalConsumer() {
        return targetFinalConsumer;
    }

    public boolean isTargetHolder() {
        return targetHolder;
    }

    public String getTargetNumberAddress() {
        return targetNumberAddress;
    }

    public String getTargetPostalCodeAddress() {
        return targetPostalCodeAddress;
    }

    public String getTargetProvinceAddress() {
        return targetProvinceAddress;
    }

    public String getTargetStreetAddress() {
        return targetStreetAddress;
    }

    public long getTotal() {
        return total;
    }

    public boolean isTransmitterHolder() {
        return transmitterHolder;
    }

    public void addProduct(CotProduct product) {
        if (product == null) {
            throw new IllegalArgumentException("Can't add null product to shipping.");
        }
        products.put(product.getProductCode(), product);
    }

    @Override
    public String toString() {
        return "CotShipping{" + "date=" + date + ", code=" + code + ", releaseDate=" + releaseDate + ", subject=" + subject + ", targetFinalConsumer=" + targetFinalConsumer + ", targetHolder=" + targetHolder + ", targetStreetAddress=" + targetStreetAddress + ", targetNumberAddress=" + targetNumberAddress + ", targetPostalCodeAddress=" + targetPostalCodeAddress + ", targetCityAddress=" + targetCityAddress + ", targetProvinceAddress=" + targetProvinceAddress + ", deliveryHomeAddress=" + deliveryHomeAddress + ", sourceCUIT=" + sourceCUIT + ", sourceSocialReason=" + sourceSocialReason + ", transmitterHolder=" + transmitterHolder + ", sourceStreetAddress=" + sourceStreetAddress + ", sourceNumberAddress=" + sourceNumberAddress + ", sourcePostalCodeAddress=" + sourcePostalCodeAddress + ", sourceCityAddress=" + sourceCityAddress + ", sourceProvinceAddress=" + sourceProvinceAddress + ", shipperCUIT=" + shipperCUIT + ", total=" + total + ", products=" + products + '}';
    }

}
