package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
    JMenuItem duplicateSelection;
	
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
		KeyListener keyPressListener = new KeyPressListener();
		
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
	    duplicateSelection = new JMenuItem("Duplicate selection");
	    
	    deleteSelection.addActionListener(buttonListener);
	    moveSelectionUp.addActionListener(buttonListener);
	    moveSelectionDown.addActionListener(buttonListener);
	    duplicateSelection.addActionListener(buttonListener);
	    
	    tableMenu.add(deleteSelection);
	    tableMenu.add(moveSelectionUp);
	    tableMenu.add(moveSelectionDown);
	    tableMenu.add(duplicateSelection);
	    
	    fileTable.setComponentPopupMenu(tableMenu);
	    
	    fileTable.addKeyListener(keyPressListener);
	    	    
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
	
	private void deleteSelectedRows() {
		int selectedRow = fileTable.getSelectedRow();
		if(selectedRow > -1) {
			int[] selectedRows = fileTable.getSelectedRows();
			if(workspace.removeFilesFromWorkspace(selectedRows))
				tableModel.fireTableRowsDeleted(selectedRows[0], selectedRows[selectedRows.length - 1]);
		}
	}
	
	private void moveSelectedRowsUp() {
		int selectedRow = fileTable.getSelectedRow();
		if(selectedRow > 0) {
			int[] selectedRows = fileTable.getSelectedRows();
			if(workspace.moveFilesUp(selectedRows)) {
				tableModel.fireTableDataChanged();
				fileTable.setRowSelectionInterval(selectedRows[0] - 1, selectedRows[selectedRows.length - 1] - 1); // Move selection upwards
			}
		}
	}

	private void moveSelectedRowsDown() {
		int selectedRow = fileTable.getSelectedRow();
		if(selectedRow > -1) {
			int[] selectedRows = fileTable.getSelectedRows();
			if(workspace.moveFilesDown(selectedRows)) {
				tableModel.fireTableDataChanged();
				fileTable.setRowSelectionInterval(selectedRows[0] + 1, selectedRows[selectedRows.length - 1] + 1); // Move selection downwards
			}
		}
	}
	
	private void duplicateSelectedRows() {
		int [] selectedRows = fileTable.getSelectedRows();
		int rows = PdfWorkspace.totalFiles;
		
		workspace.duplicateFiles(selectedRows);
		
		tableModel.fireTableDataChanged();
		fileTable.setRowSelectionInterval(rows, rows + selectedRows.length - 1);
		
	}
		
	
	class ButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource().equals(openFilesButton)) { // Open files and add them into the workspace
				
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
			
			}else if(arg0.getSource().equals(mergeFilesButton)) { // Merge files

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
				
				
			}else if(arg0.getSource().equals(deleteSelection)) 
				deleteSelectedRows();
			else if(arg0.getSource().equals(moveSelectionUp)) 
				moveSelectedRowsUp();
			else if(arg0.getSource().equals(moveSelectionDown)) 
				moveSelectedRowsDown();
			else if(arg0.getSource().equals(duplicateSelection))
				duplicateSelectedRows();
		}	
	}
	
	class KeyPressListener implements KeyListener {

		// Currently pressed keys
	    private final Set<Integer> pressed = new HashSet<Integer>(); // We use a Set to guarantee key uniqueness

		@Override
		public void keyPressed(KeyEvent e) {
			pressed.add(e.getKeyCode()); // On press add pressed key to set
			
			if(pressed.size() > 1) { // Multi key events
				
				// "Move" events
				if(pressed.contains(KeyEvent.VK_ALT) && pressed.contains(KeyEvent.VK_UP))
					moveSelectedRowsUp();
				else if(pressed.contains(KeyEvent.VK_ALT) && pressed.contains(KeyEvent.VK_DOWN))
					moveSelectedRowsDown();
				
				// "Duplicate selection" event
				if(pressed.contains(KeyEvent.VK_CONTROL) && pressed.contains(KeyEvent.VK_D))
					duplicateSelectedRows();
				
			}else { // Single key events
				
				// "Delete" event
				if(e.getKeyCode() == KeyEvent.VK_DELETE) 
					deleteSelectedRows();
				
				// "Clear selection" event
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
					fileTable.clearSelection();
			}
			
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			pressed.remove(e.getKeyCode());
		}

		@Override
		public void keyTyped(KeyEvent e) {}
		
	}
}
