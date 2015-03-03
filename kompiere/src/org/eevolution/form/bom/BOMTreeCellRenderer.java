package org.eevolution.form.bom;

import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.compiere.util.Env;

import org.eevolution.form.tree.MapTreeCellRenderer;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class BOMTreeCellRenderer extends MapTreeCellRenderer {

	public BOMTreeCellRenderer(HashMap map) {
		
		super(map);
	}

    protected ImageIcon getIcon(Object value) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

    	ImageIcon icon = null;
        if(node.isLeaf()) {
        	
        	icon = Env.getImageIcon("Product10.gif");
        }

        return icon;
    }
}	