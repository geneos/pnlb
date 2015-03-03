/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.table;

// This subclass adds a method to retrieve the columnIdentifiers

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

// which is needed to implement the removal of
// column data from the table model
public class XTableModel extends DefaultTableModel {
    public Vector getColumnIdentifiers() {
        return columnIdentifiers;
    }
}
