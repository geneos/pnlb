package org.eevolution.form;

import org.eevolution.model.wrapper.BOMWrapper;

/**
 * Order BOM Tree is based on MPCOrderBOM.
 *
 * AD_FORM:
 * INSERT INTO ad_form VALUES (1000024, 0, 0, 'Y', CURRENT_TIMESTAMP, 100, CURRENT_TIMESTAMP, 100, 'Order BOM Tree', NULL, NULL, '3', 'org.compiere.mfg.form.COrderBOMTree', 'U', 'N', NULL);
 *
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class COrderBOMTree extends CAbstractBOMTree {

	public COrderBOMTree() {

		super();
	}

	protected String type() {

		return BOMWrapper.BOM_TYPE_ORDER;
	}
}
