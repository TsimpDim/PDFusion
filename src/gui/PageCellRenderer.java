package gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class PageCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -2628784840588878213L;

	public Component getTableCellRendererComponent(
                        JTable table, Object value,
                        boolean isSelected, boolean hasFocus,
                        int row, int column) {
        JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String pages = table.getValueAt(row, column).toString();

        c.setToolTipText("<html><i>e.g<i> 1,2,4-10,16,17-19" + "<br><br><div width=\"200\" style=\"word-break:break-all\"><strong>Selected pages : <strong>" + pages + "</div></html>");
        return c;
    }
}
