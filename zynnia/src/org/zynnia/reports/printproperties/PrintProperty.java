/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.printproperties;

import org.zynnia.reports.enums.IdEnum;

public enum PrintProperty implements IdEnum {

	LABEL_PROPERTY(0, "Título"),
	ALIGN_PROPERTY(1, "Alineación"),
	WIDTH_PROPERTY(2, "Ancho Columna");
	
	private int value;
	private String label;
	private PrintProperty(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public int getId() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public boolean equals(PrintProperty prop) {
		if (prop == this) {
			return true;
		} else if (prop!=null && prop.getId() == getId()) {
			return true;
		}
		return false;
	}
}
