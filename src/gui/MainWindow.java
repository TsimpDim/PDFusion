package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.PdfFile;
import control.PdfWorkspace;

public class MainWindow extends JFrame{


	private static final long serialVersionUID = -6007278116878081383L;

	PdfWorkspace workspace = null;
	
	JPanel container;
	
	JPanel sidePanel;
	JButton openFilesButton;
	JButton mergeFilesButton;
	JFileChooser fileChooser;
	
	JTable fileTable;
	JScrollPane fileTablePane;
	PdfFileTableModel tableModel;
    String[] tableColumnNames = {"id","path","include","pages"};
    JPopupMenu tableMenu;
    JMenuItem deleteSelection;
    JMenuItem moveSelectionUp;
    JMenuItem moveSelectionDown;
	
	public MainWindow(PdfWorkspace works) {
		
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }
		
	    // Base declarations
		workspace = works;
		
		container = new JPanel();
		ButtonListener buttonListener = new ButtonListener();
		
		// Side panel
		sidePanel = new JPanel();
		openFilesButton = new JButton("Choose files");
		mergeFilesButton = new JButton("Merge files");
	
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");
		fileChooser.setFileFilter(filter);
		
	    openFilesButton.addActionListener(buttonListener);
	    mergeFilesButton.addActionListener(buttonListener);
	    
	    // Table
	    fileTable = new JTable();
	    tableModel = new PdfFileTableModel(null);
	    fileTable.setFillsViewportHeight(true);
	    fileTable.setModel(tableModel);
	    
	    fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	    fileTable.getColumnModel().getColumn(0).setPreferredWidth(20);
	    fileTable.getColumnModel().getColumn(1).setPreferredWidth(741);
	    fileTable.getColumnModel().getColumn(2).setPreferredWidth(60);
	    fileTable.getColumnModel().getColumn(3).setPreferredWidth(60);
	    
	    fileTable.getColumnModel().getColumn(2).setCellRenderer(new PageCellRenderer(works));
	    
	    fileTablePane = new JScrollPane(fileTable);
	    
	    tableMenu = new JPopupMenu();
	    deleteSelection = new JMenuItem("Delete file(s)");
	    moveSelectionUp = new JMenuItem("Move up");
	    moveSelectionDown = new JMenuItem("Move down");
	    
	    deleteSelection.addActionListener(buttonListener);
	    moveSelectionUp.addActionListener(buttonListener);
	    moveSelectionDown.addActionListener(buttonListener);
	    
	    tableMenu.add(deleteSelection);
	    tableMenu.add(moveSelectionUp);
	    tableMenu.add(moveSelectionDown);
	    
	    fileTable.setComponentPopupMenu(tableMenu);
	    
	    // Component setup
	    sidePanel.add(openFilesButton);
	    sidePanel.add(mergeFilesButton);
	    
	    container.setLayout(new BorderLayout());
	    container.add(sidePanel, BorderLayout.WEST);
	    container.add(fileTablePane, BorderLayout.CENTER);
	    
		this.setContentPane(container);
		
		this.setTitle("Fusion Workspace");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,500);
	}
	
	
	class ButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource().equals(openFilesButton)) {
				
				int returnVal = fileChooser.showOpenDialog(MainWindow.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File[] new_files = fileChooser.getSelectedFiles();
					
					for(File file : new_files) {
						String curPath = file.getPath();
						int fileIndex = PdfWorkspace.totalFiles;
						
						PdfFile newPDF = new PdfFile(curPath, true, fileIndex);
						workspace.addFileToWorkspace(newPDF);
					}
					tableModel.updateData(workspace.getAllFiles());
				}
			
			}else if(arg0.getSource().equals(mergeFilesButton)) {

				fileChooser.setSelectedFile(new File("export.pdf")); // Sets default filename
				
				int returnVal = fileChooser.showSaveDialog(MainWindow.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				    
				    String filename = fileChooser.getSelectedFile().getName().toLowerCase();
				    String destination = fileChooser.getSelectedFile().getPath();
				    
				    if (!filename.endsWith(".pdf")) {
				      filename += ".pdf";
				    }
				      
					workspace.mergeFiles(filename, destination);
				}
				
				
			}else if(arg0.getSource().equals(deleteSelection)) {
				int selectedRow = fileTable.getSelectedRow();
				if(selectedRow > -1) {
					int[] selectedRows = fileTable.getSelectedRows();
					if(workspace.removeFilesFromWorkspace(selectedRows))
						tableModel.fireTableRowsDeleted(selectedRows[0], selectedRows[selectedRows.length - 1]);
				}
			}else if(arg0.getSource().equals(moveSelectionUp)) {
				int[] selectedRows = fileTable.getSelectedRows();
				if(workspace.moveFilesUp(selectedRows)) {
					tableModel.fireTableDataChanged();
					fileTable.setRowSelectionInterval(selectedRows[0] - 1, selectedRows[selectedRows.length - 1] - 1); // Move selection upwards
				}
			}else if(arg0.getSource().equals(moveSelectionDown)) {
				int[] selectedRows = fileTable.getSelectedRows();
				if(workspace.moveFilesDown(selectedRows)) {
					tableModel.fireTableDataChanged();
					fileTable.setRowSelectionInterval(selectedRows[0] + 1, selectedRows[selectedRows.length - 1] + 1); // Move selection downwards
				}
			}
		}
	}
}
