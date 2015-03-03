/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.printproperties;

import java.beans.PropertyChangeEvent;
import org.compiere.grid.ed.VEditor;
import org.compiere.model.MField;
import org.compiere.swing.CComboBox;

public class VCombo extends CComboBox implements VEditor {
	public VCombo (String[] values) {
		super (values);
		super.setName("Combo-Editor");
	}

	public void setField(MField mField) {
	}

	public void dispose() { }

	public void propertyChange(PropertyChangeEvent evt) {
		setValue(evt.getNewValue());
	}
}	