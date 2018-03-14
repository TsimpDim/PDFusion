package gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import control.PdfFile;

public class PdfFileTableModel extends AbstractTableModel{

	private String[] columnNames = {"id", "path", "pages", "include"};
	private ArrayList<PdfFile> files;
	
	PdfFileTableModel(ArrayList<PdfFile> files){
		super();
		this.files = files;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(files == null) 
			return 0;
		else
			return files.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		PdfFile req_file = files.get(row);
		
		switch(col) {
		case 0: 
			return req_file.getFileId();
		case 1:
			return req_file.getPath();
		case 2:
			return req_file.getPagesString();
		case 3:
			return req_file.getToMerge();
		default:
			return null;
		
		}
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
	
	public void updateData(ArrayList<PdfFile> newFiles) {
		this.files = newFiles;
	}

}
