package org.eevolution.form;

import org.eevolution.model.wrapper.BOMWrapper;

/**
 * Product BOM Tree is based on MPCProductBOM.
 * 
 * AD_FORM:
 * INSERT INTO ad_form VALUES (1000023, 0, 0, 'Y', CURRENT_TIMESTAMP, 100, CURRENT_TIMESTAMP, 100, 'Product BOM Tree', NULL, NULL, '3', 'org.compiere.mfg.form.CProductBOMTree', 'U', 'N', NULL);
 *
 * 
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class CProductBOMTree extends CAbstractBOMTree {

	public CProductBOMTree() {

		super();
	}

	protected String type() {

		return BOMWrapper.BOM_TYPE_PRODUCT;
	}
}