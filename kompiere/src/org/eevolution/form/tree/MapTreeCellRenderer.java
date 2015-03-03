package org.eevolution.form.tree;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
* @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
* @version 1.0, October 14th 2005
*/
public abstract class MapTreeCellRenderer extends DefaultTreeCellRenderer {

	HashMap map;
	
    protected abstract ImageIcon getIcon(Object value);
	
	public MapTreeCellRenderer(HashMap map) {
		
		this.map = new HashMap();
		this.map.putAll(map);
	}
	
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    	
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        String name = (String)getMapping(value);
        setText(name);
        ImageIcon icon = getIcon(value);
        setIcon(icon);

        return this;
    }
    
    protected Object getMapping(Object value) {
    	
    	return map.get(value);
    }
}	