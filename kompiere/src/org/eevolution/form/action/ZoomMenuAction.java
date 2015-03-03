package org.eevolution.form.action;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.model.MQuery;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.eevolution.model.wrapper.AbstractPOWrapper;
import org.compiere.util.Env;


/**
 * Zoom Menu Action
 * 
 * Zooms directly to a static destination window referred from action's properties or to a dynamic destination,
 * dependent on action's instantiation w/o or with a target component.
 * 
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class ZoomMenuAction extends PopupAction {

	public static final String COMMAND = "zoom";
	
	protected Object target;
	protected int tableID;
	protected String tableName;
	
	/**
	 * Constructs a new Instance with static zoom target, determined by . The properties are used to determine the
	 * zoom target. 
	 */
	public ZoomMenuAction(int tableID, String tableName) {
		
		super(COMMAND);
		setActionCommand(COMMAND);
		
		this.tableID = tableID;
		this.tableName = tableName;
	}
	
	/**
	 * Constructs a new Instance with a overgiven target. E.g. use your JTree as target object. The nodes 
	 * of your tree owns their compiere model objects (PO) as user objects. 
	 * Supported classes are: JTree.  
	 * 
	 * @param target the target object.
	 * @throws a exception, if the target class isn't supported.
	 */
	public ZoomMenuAction(Object target) throws Exception {

		super(COMMAND);
		setActionCommand(COMMAND);
	
		if(target != null && !(target instanceof JTree)) {
		
			throw new Exception("Unsupported target component: "+ target.getClass().getName());
		}
		
		this.target = target;
	}
	
	protected String getCommand() {

		return COMMAND;
	}

	protected String validateAction() {

		return null;
	}
	
	protected void doAction(ActionEvent e) {

		if(target != null) {
			
			zoom(target);
		}
		else {
			
			zoom();
		}
	}

	protected boolean successful() {
		
		return true;
	}
	
	public Object getTarget() {
		
		return target;
	}
	
	private void zoom(Object obj) {

		if(obj instanceof JTree) {

		    JTree tree = (JTree)obj;
			
			Object node = tree.getSelectionPath().getLastPathComponent();
			
			int tableId = 0;
			int recordId = 0;
			try {

				tableId = getTableID((DefaultMutableTreeNode)node);
				recordId = getRecordID((DefaultMutableTreeNode)node);
			}
			catch(Exception e) {
				
				e.printStackTrace();
			}
			
			AEnv.zoom(tableId, recordId);
		}
	}
	
	private int getTableID(DefaultMutableTreeNode tn) throws Exception {
		
		PO po = null;
		if(tn.getUserObject() instanceof PO) {
			
			po = (PO)tn.getUserObject();
		}
		else if(tn.getUserObject() instanceof AbstractPOWrapper) {
			
			po = ((AbstractPOWrapper)tn.getUserObject()).get();
		}
		else {
			
			return -1;
		}

		Field f = po.getClass().getField("Table_ID");
		
		return f.getInt(null);
	}

	private int getRecordID(DefaultMutableTreeNode tn) {
		
		PO po = null;
		if(tn.getUserObject() instanceof PO) {
			
			po = (PO)tn.getUserObject();
		}
		else if(tn.getUserObject() instanceof AbstractPOWrapper) {
			
			po = ((AbstractPOWrapper)tn.getUserObject()).get();
		}
		
		return po == null ? -1 : po.get_ID();
	}
	
	private void zoom() {
		
		String tablename = tableName;
		int tableid = tableID;
		
		MQuery query = new MQuery();
		query.setTableName(tablename);

		AWindow window = new AWindow();
		if (window.initWindow(tableid, query)) {
		
			AEnv.showCenterScreen(window);
		}

		window = null;
	}
}

