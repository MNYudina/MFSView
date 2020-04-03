package io.github.mnyudina.motifVis;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
/**
 * 
 * @author Gleepa
 *
 */
public class MyPictureCellRenderer implements TableCellRenderer {
	int h=50;
	public void setHeight(int h) {
		this.h=h;
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		table.setRowHeight(h);
		return (Component)value;
	}

}
