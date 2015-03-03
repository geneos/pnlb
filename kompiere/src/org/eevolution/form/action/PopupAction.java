package org.eevolution.form.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;

import org.compiere.model.PO;

import org.eevolution.tools.PLoader;
import org.eevolution.tools.worker.SingleWorker;
import org.eevolution.tools.swing.SwingTool;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public abstract class PopupAction extends JMenuItem implements ActionListener {

	protected final ResourceBundle language;

	protected PropertyChangeSupport propertyChange;
	protected boolean success;
	protected boolean ignoreChange;
	protected SingleWorker worker;
	protected String errorMsg;
	
	protected abstract void doAction(ActionEvent e);
	protected abstract String getCommand();
	protected abstract String validateAction();
	
	
	protected boolean successful() {
		
		return success;
	}

	public String getSuccessMsg() {
		
		return "OK";
	}
	
	protected void setError(String msg) {
		
		errorMsg = msg;
		success = false;
	}

	public String getErrorMsg() {
		
		return errorMsg;
	}
	
	public PopupAction(String property) {
		
		super();
		
		language = ResourceBundle.getBundle(getClass().getPackage().getName()+".language");
		setText(language.getString(property));
		setActionCommand(getCommand());

		init();

		addActionListener(this);
	}

	protected void init() {
		
		this.success = true;
		this.ignoreChange = false;
	}
	
	protected void beforeAction() {
		
		init();
		
		String valid = validateAction();
		if(valid != null) {
			
			setError(valid);
			return;
		}
		
		SwingTool.setCursorsFromChild(this, true);
	}

	protected void afterAction() {
	
		if(!isIgnoreChange()) {
			
			propertyChange.firePropertyChange(getCommand(), false, successful());
		}
		
		SwingTool.setCursorsFromChild(this, false);
	}
	
	public void actionPerformed(ActionEvent e) {

		final ActionEvent evt = e;
		worker = new SingleWorker() {
			
			protected Object doIt() {
				
				run(evt);
				return null;
			};
		};
		worker.start();
	}
	
	protected void run(ActionEvent e) {
		
		beforeAction();
		if(getActionCommand() != null && getActionCommand().equals(e.getActionCommand())) {
			
			doAction(e);
		}
		afterAction();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		
		if(propertyChange == null) {
			
			propertyChange = new PropertyChangeSupport(this);
		}
		propertyChange.addPropertyChangeListener(listener);
	}
	
	protected void setIgnoreChange(boolean ignore) {
		
		ignoreChange = ignore;
	}
	
	public boolean isIgnoreChange() {
		
		return ignoreChange;
	}
	
	protected void savePO(PO po) {
		
		success = po.save(null);
	}

	protected void deletePO(PO po) {
		
		success = po.delete(true, null);
	}
}
