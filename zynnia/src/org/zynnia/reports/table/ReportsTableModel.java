/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports.table;

import javax.swing.table.AbstractTableModel;

public class ReportsTableModel extends AbstractTableModel {

	private String[] columnNames = {"the_city", "id", "name", "street"};
	private Object[][] data = {
		{"Berne", "22", "Bill Ott", "250 - 20th Ave."},
		{"Berne", "9", "James Schneider", "277 Seventh Av."},
		{"Boston", "32", "Michael Ott", "339 College Av."},
		{"Boston", "23", "Julia Heiniger", "358 College Av."},
		{"Chicago", "39", "Mary Karsen", "202 College Av."},
		{"Chicago", "35", "George Karsen", "412 College Av."},
		{"Chicago", "11", "Julia White", "412 Upland Pl."},
		{"Dallas", "47", "Janet Fuller", "445 Upland Pl."},
		{"Dallas", "43", "Susanne Smith", "2 Upland Pl."},
		{"Dallas", "40", "Susanne Miller", "440 - 20th Ave."},
		{"Dallas", "36", "John Steel", "276 Upland Pl."},
		{"Dallas", "37", "Michael Clancy", "19 Seventh Av."},
		{"Dallas", "19", "Susanne Heiniger", "86 - 20th Ave."},
		{"Dallas", "10", "Anne Fuller", "135 Upland Pl."},
		{"Dallas", "4", "Sylvia Ringer", "365 College Av."},
		{"Dallas", "0", "Laura Steel", "429 Seventh Av."},
		{"Lyon", "38", "Andrew Heiniger", "347 College Av."},
		{"Lyon", "28", "Susanne White", "74 - 20th Ave."},
		{"Lyon", "17", "Laura Ott", "443 Seventh Av."},
		{"Lyon", "2", "Anne Miller", "20 Upland Pl."},
		{"New York", "46", "Andrew May", "172 Seventh Av."},
		{"New York", "44", "Sylvia Ott", "361 College Av."},
		{"New York", "41", "Bill King", "546 College Av."},
		{"Oslo", "45", "Janet May", "396 Seventh Av."},
		{"Oslo", "42", "Robert Ott", "503 Seventh Av."},
		{"Paris", "25", "Sylvia Steel", "269 College Av."},
		{"Paris", "18", "Sylvia Fuller", "158 - 20th Ave."},
		{"Paris", "5", "Laura Miller", "294 Seventh Av."},
		{"San Francisco", "48", "Robert White", "549 Seventh Av."},
		{"San Francisco", "7", "James Peterson", "231 Upland Pl."}
	};

	public ReportsTableModel() {
	}

	public int getColumnCount() {
		return this.columnNames.length;
	}

	public String getColumnName(int columnIndex) {
		return this.columnNames[columnIndex];
	}

	public int getRowCount() {
		return this.data.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.data[rowIndex][columnIndex];
	}
}
