/*
 * Copyright (C) 2004 Nicky BRAMANTE
 * 
 * This file is part of FreeQueryBuilder
 * 
 * FreeQueryBuilder is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Send questions or suggestions to nickyb@interfree.it
 */

package nickyb.fqb;

import nickyb.fqb.util.*;

import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.TableCellEditor;

public class ExpressionCellEditor extends AbstractCellEditor implements ActionListener, TableCellEditor
{
	ExpressionCellOwner owner;
	
	private DefaultPanel cell;
	private JTextField txt;
	
	public ExpressionCellEditor(ExpressionCellOwner owner)
	{
		this.owner = owner;
		
		JButton btn = new JButton("...");
		btn.addActionListener(this);
		
		cell = new DefaultPanel();
		cell.setCenterComponent(txt = new JTextField());
		cell.setEastComponent(btn);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		new DialogExpression(this).show();
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		txt.setText(value!=null ? value.toString() : null);
		return cell;
	}

	public String getCellEditorText()
	{
		return getCellEditorValue().toString();
	}
	
	public void setCellEditorText(String text)
	{
		txt.setText(text);
		fireEditingStopped();
	}
	
	public Object getCellEditorValue()
	{
		return txt.getText();
	}
}
