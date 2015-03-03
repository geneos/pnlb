package org.eevolution.form.bom;

import java.util.HashMap;

import javax.swing.JTree;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public interface BOMTreeModel {

	public JTree getTree();
	
	public HashMap getNodeMapping();

	public BOMMessenger getBOMMessenger();
}
