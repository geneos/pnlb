package org.eevolution.form.bom;

import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.compiere.model.MProduct;
import org.eevolution.model.wrapper.BOMLineWrapper;
import org.eevolution.model.wrapper.BOMWrapper;


import org.eevolution.model.MMPCOrder;
import org.eevolution.msg.HTMLMessenger;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class BOMMessenger extends HTMLMessenger {
	
	protected JTree bomTree;
	protected BOMTreeCellRenderer bomTreeCellRenderer;
	protected HashMap cache;
	
	public BOMMessenger(JTree bomTree) {
		
		this.bomTree = bomTree;
		this.cache = new HashMap();
	}
	
	public String getToolTipText(MouseEvent evt){

		String tooltip = null;
		
		if(bomTree.getRowForLocation(evt.getX(), evt.getY()) == -1) {
		
			return tooltip;
		}

		return getToolTipText(bomTree.getPathForLocation(evt.getX(), evt.getY()));
	}
	
	public String getToolTipText(TreePath path){

		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		
	    String tooltip = (String)cache.get(node);

	    if(tooltip != null) {
	    	
	    	return tooltip;
	    }
	    
	    if(node.getUserObject() instanceof MProduct) {
	    	
			tooltip = getProductInfo((MProduct)node.getUserObject());	    	
	    }
	    if(node.getUserObject() instanceof MMPCOrder) {
	    	
			tooltip = getMfcOrderInfo((MMPCOrder)node.getUserObject());	    	
	    }
	    else if(node.getUserObject() instanceof BOMWrapper) {
	    	
	    	tooltip = getBOMInfo((BOMWrapper)node.getUserObject());
	    }
	    else if(node.getUserObject() instanceof BOMLineWrapper) {
	    	
	    	tooltip = getBOMLineInfo((BOMLineWrapper) node.getUserObject());
	    }

	    cache.put(node, tooltip);
	    
		return tooltip;
	}
	
	public static String getToolTipText(JTree tree, MouseEvent evt) {
		
		BOMMessenger msg = new BOMMessenger(tree);
		return msg.getToolTipText(evt);
	}
}
