/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import org.zynnia.reports.enums.IdEnum;

public enum ParameterType implements IdEnum {

	TYPE_TEXT(1000010),
	TYPE_DATE(1000005),
	TYPE_SEARCH(1000001),
	TYPE_SEARCH_RANGE(1000002),
	TYPE_DATE_RANGE(1000006),
	TYPE_LIST(1000007),
	TYPE_LIST_RANGE(1000008),
	TYPE_AMOUNT(1000003),
	TYPE_AMOUNT_RANGE(1000004),
	TYPE_SELECT(1000009),
        TYPE_LISTFIX(1000011);
	
	private int value;

	private ParameterType(int value) {
		this.value = value;
	}

	public int getId() {
		return value;
	}
}
