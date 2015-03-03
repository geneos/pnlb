package org.eevolution.form.crp;

import javax.swing.JTree;

import org.jfree.data.category.CategoryDataset;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public interface CRPModel {

	public JTree getTree();
	
	public CategoryDataset getDataset();
}
