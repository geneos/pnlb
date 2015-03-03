package org.eevolution.form.bom.action;

import java.awt.event.ActionEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eevolution.form.action.PopupAction;
import org.eevolution.model.wrapper.BOMLineWrapper;
import org.eevolution.model.wrapper.BOMWrapper;

/**
 * Delete Bill of Material / Line
 * 
 * Deletes a bill of material or a single line by tree selection.
 * 
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class DeleteBOMAction extends PopupAction {

	public static final String COMMAND = "deleteBOM";
	
	protected JTree tree;
	
	/**
	 * Constructs a new Instance.
	 */
	public DeleteBOMAction(JTree tree) {
		
		super(COMMAND);
		setActionCommand(COMMAND);
		
		this.tree = tree;
	}

	protected String getCommand() {

		return COMMAND;
	}

	protected String validateAction() {

		return null;
	}

	protected void doAction(ActionEvent e) {
	
		if(tree != null) {

			delete((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
		}
	}

	private void delete(DefaultMutableTreeNode node) {
		
		if(node.getUserObject() instanceof BOMWrapper) {
			
			BOMWrapper bom = (BOMWrapper)node.getUserObject();
	    	for(int i = 0; i < node.getChildCount(); i++) {
	    		
	    		delete((DefaultMutableTreeNode)node.getChildAt(i));
	    		if(!successful()) {
	    			
	    			break;
	    		}
	    	}
 
	    	deletePO(bom.get());
		}
		else {
			
	    	BOMLineWrapper line = (BOMLineWrapper)node.getUserObject();
	    	deletePO(line.get());
		}
	}
}
