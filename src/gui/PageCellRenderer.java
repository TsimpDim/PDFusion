package gui;

import control.PdfWorkspace;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class PageCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -2628784840588878213L;
	private PdfWorkspace works;
	
	public PageCellRenderer(PdfWorkspace works) {
		super();
		this.works = works;
	}
	
	public Component getTableCellRendererComponent(
                        JTable table, Object value,
                        boolean isSelected, boolean hasFocus,
                        int row, int column) {
		
        JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String pages = table.getValueAt(row, column).toString();
        Integer availablePages = works.getFile(row).getNumberOfAvailablePages();
        String availablePagesString = String.valueOf(availablePages);

        c.setToolTipText("<html><i>e.g<i> 1,2,4-10,16,17-19" + "<br><br>"
        		+ "<div width=\"200\" style=\"word-break:break-all\"><br>"
        		+ "<strong>Available pages : <strong>" + availablePagesString + "</div>"
        		+ "<strong>Selected pages : <strong>" + pages + "</div>"
        		+ "</html>");
        
        return c;
    }
}
