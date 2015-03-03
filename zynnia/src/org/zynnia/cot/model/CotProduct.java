package org.zynnia.cot.model;

import org.zynnia.cot.utils.CotFormatterUtils;

/**
 *
 * @author alejandro
 */
public class CotProduct {

     private final String cotCode;

     private final double amount;

     private final String productCode;

     private final String descriptionProduct;

     private final String codeMeasureUnit;

     private final String descriptionMeasureUnit;

    public CotProduct(String cotCode, double amount, String productCode, String descriptionProduct, String codeMeasureUnit, String descriptionMeasureUnit) {
        this.cotCode = cotCode;
        this.amount = amount;
        this.productCode = productCode;
        this.descriptionProduct = descriptionProduct;
        this.codeMeasureUnit = codeMeasureUnit;
        this.descriptionMeasureUnit = descriptionMeasureUnit;
    }

    public double getAmount() {
        return amount;
    }

    public String getCOTCode() {
        return cotCode;
    }

    public String getCodeMeasureUnit() {
        return codeMeasureUnit;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getDescriptionMeasureUnit() {
        return descriptionMeasureUnit;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(cotCode);
        sb.append(CotFormatterUtils.COT_SEPARATOR);
        sb.append(codeMeasureUnit);
        sb.append(CotFormatterUtils.COT_SEPARATOR);
        sb.append(amount);
        sb.append(CotFormatterUtils.COT_SEPARATOR);
        sb.append(productCode);
        sb.append(CotFormatterUtils.COT_SEPARATOR);
        sb.append(CotFormatterUtils.getFormattedComment(descriptionProduct, 40));
        sb.append(CotFormatterUtils.COT_SEPARATOR);
        sb.append(CotFormatterUtils.getFormattedComment(descriptionMeasureUnit, 20));
        sb.append(CotFormatterUtils.COT_SEPARATOR);
        sb.append(CotFormatterUtils.getFormattedAmount(amount));

        return sb.toString();
    }
}
