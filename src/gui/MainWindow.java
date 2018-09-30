package gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.PdfFile;
import control.PdfWorkspace;

public class MainWindow extends JFrame{


	private static final long serialVersionUID = -6007278116878081383L;

	PdfWorkspace workspace = null;
	
	JPanel container;
	
	JPanel sidePanel;
	JFileChooser fileChooser;

	JTable fileTable;
	JScrollPane fileTablePane;
	PdfFileTableModel tableModel;
	String[] tableColumnNames = {"id","path","include","pages"};
	JPopupMenu tableMenu;

	JMenuBar menuBar;
	JMenu selectionMenu;
	JMenu openMenu;
	JMenu editMenu;

	JMenuItem openFiles;
	JMenuItem mergeFiles;

	// These go in the header
	JMenuItem deleteSelectionMenuBar;
    JMenuItem moveSelectionUpMenuBar;
    JMenuItem moveSelectionDownMenuBar;
    JMenuItem duplicateSelectionMenuBar;
    JMenuItem undoDeletionMenubar;

    // These go in the right-click (pop up)menu
	JMenuItem deleteSelectionTable;
	JMenuItem moveSelectionUpTable;
	JMenuItem moveSelectionDownTable;
	JMenuItem duplicateSelectionTable;
	JMenuItem undoDeletionTable;
	
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
		menuBar = new JMenuBar();

		// Open Menu
		openMenu = new JMenu("Open");
		openMenu.setMnemonic(KeyEvent.VK_O);
		openFiles = new JMenuItem("Open files");
		openFiles.addActionListener(buttonListener);
		openFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

		// Selection Menu
		selectionMenu = new JMenu("Selection");
		selectionMenu.setMnemonic(KeyEvent.VK_S);

		deleteSelectionMenuBar = new JMenuItem("Delete file(s)");
		deleteSelectionMenuBar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

		moveSelectionUpMenuBar = new JMenuItem("Move up");
		moveSelectionUpMenuBar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK));

		moveSelectionDownMenuBar = new JMenuItem("Move down");
		moveSelectionDownMenuBar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK));

		duplicateSelectionMenuBar = new JMenuItem("Duplicate selection");
		duplicateSelectionMenuBar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));

		undoDeletionMenubar = new JMenuItem("Undo previous deletion");
		undoDeletionMenubar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));


		deleteSelectionMenuBar.addActionListener(buttonListener);
		moveSelectionUpMenuBar.addActionListener(buttonListener);
		moveSelectionDownMenuBar.addActionListener(buttonListener);
		duplicateSelectionMenuBar.addActionListener(buttonListener);
		undoDeletionMenubar.addActionListener(buttonListener);

		// Edit Menu
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		mergeFiles = new JMenuItem("Merge files");
		mergeFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.CTRL_MASK));

		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");
		fileChooser.setFileFilter(filter);

		mergeFiles.addActionListener(buttonListener);


		// Table
		fileTable = new JTable();
		tableModel = new PdfFileTableModel(null);
		fileTable.setFillsViewportHeight(true);
		fileTable.setModel(tableModel);

		PageCellRenderer leftAlignedRenderer = new  PageCellRenderer(works);
		leftAlignedRenderer.setHorizontalAlignment(JLabel.LEFT);

		fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		fileTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		fileTable.getColumnModel().getColumn(0).setCellRenderer(leftAlignedRenderer);
		fileTable.getColumnModel().getColumn(1).setPreferredWidth(741);
		fileTable.getColumnModel().getColumn(2).setPreferredWidth(60);
		fileTable.getColumnModel().getColumn(3).setPreferredWidth(60);
		fileTable.getColumnModel().getColumn(2).setCellRenderer(new PageCellRenderer(works));
		fileTable.setAutoCreateRowSorter(true);
		fileTablePane = new JScrollPane(fileTable);

		// Table Right-Click Menu
		tableMenu = new JPopupMenu();

		deleteSelectionTable = new JMenuItem("Delete file(s)");
		moveSelectionUpTable = new JMenuItem("Move up");
		moveSelectionDownTable = new JMenuItem("Move down");
		duplicateSelectionTable = new JMenuItem("Duplicate selection");
		undoDeletionTable = new JMenuItem("Undo previous deletion");

		deleteSelectionTable.addActionListener(buttonListener);
		moveSelectionUpTable.addActionListener(buttonListener);
		moveSelectionDownTable.addActionListener(buttonListener);
		duplicateSelectionTable.addActionListener(buttonListener);
		undoDeletionTable.addActionListener(buttonListener);

		tableMenu.add(deleteSelectionTable);
		tableMenu.add(moveSelectionUpTable);
		tableMenu.add(moveSelectionDownTable);
		tableMenu.add(duplicateSelectionTable);
		tableMenu.add(undoDeletionTable);

		fileTable.setComponentPopupMenu(tableMenu);


	    // Component setup
		openMenu.add(openFiles);

		editMenu.add(mergeFiles);

		selectionMenu.add(deleteSelectionMenuBar);
		selectionMenu.add(moveSelectionUpMenuBar);
		selectionMenu.add(moveSelectionDownMenuBar);
		selectionMenu.add(duplicateSelectionMenuBar);
		selectionMenu.add(undoDeletionMenubar);

		menuBar.add(openMenu);
		menuBar.add(editMenu);
		menuBar.add(selectionMenu);

	    container.setLayout(new BorderLayout());
	    container.add(menuBar, BorderLayout.PAGE_START);
	    container.add(fileTablePane, BorderLayout.CENTER);
	    
		this.setContentPane(container);
		
		this.setTitle("PDFusion Workspace");
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_16.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_20.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_32.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_40.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_64.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_128.png")).getImage());
		this.setIconImages(icons);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,500);
	}
	
	/**
	 * Deletes selected rows from the workspace only if it is possible
	 */
	private void deleteSelectedRows() {
		int selectedRow = fileTable.getSelectedRow();
		if(selectedRow > -1) {
			int[] selectedRows = fileTable.getSelectedRows();

			// Save removed files to the workspace for chance of undoing the deletion
			for(int row : selectedRows){
				workspace.addRemovedFile(workspace.getFile(row));
			}

			if(workspace.removeFilesFromWorkspace(selectedRows)) {
				tableModel.fireTableRowsDeleted(selectedRows[0], selectedRows[selectedRows.length - 1]);

				// If the delete file was the only file then we have nothing to select
				if(workspace.totalFiles == 0)
					return;

				try{
					fileTable.setRowSelectionInterval(selectedRow, selectedRow);
				}catch(java.lang.IllegalArgumentException e){
					fileTable.setRowSelectionInterval(selectedRow -1, selectedRow -1);
				}
			}
		}
	}
	
	/**
	 * Moves selected higher on the workspace only if it is possible
	 */
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

	/**
	 * Moves selected lower on the workspace only if it is possible
	 */
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
	
	/**
	 * Duplicates selected rows
	 */
	private void duplicateSelectedRows() {
		int [] selectedRows = fileTable.getSelectedRows();
		int rows = PdfWorkspace.totalFiles;
		
		workspace.duplicateFiles(selectedRows);
		
		tableModel.fireTableDataChanged();
		fileTable.setRowSelectionInterval(rows, rows + selectedRows.length - 1);

	}

	/**
	 * Undoes the last deletion
	 */
	private void undoPreviousDeletion(){
		workspace.undoPreviousDeletion();
		tableModel.fireTableDataChanged();
	}
		
	
	class ButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource().equals(openFiles)) { // Open files and add them into the workspace
				
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
			
			}else if(arg0.getSource().equals(mergeFiles)) { // Merge files

				fileChooser.setSelectedFile(new File("export.pdf")); // Sets default filename
				
				int returnVal = fileChooser.showSaveDialog(MainWindow.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				    
				    String filename = fileChooser.getSelectedFile().getName().toLowerCase();
				    String destination = fileChooser.getSelectedFile().getPath();
				    
				    if (!filename.endsWith(".pdf")) {
				      filename += ".pdf";
				      destination += ".pdf";
				    }
				      
					// Initialize progress bar
					ResultsWindow progBar = new ResultsWindow(PdfWorkspace.totalFilesToMerge, "Preparing files...", filename, destination);
					workspace.mergeFiles(filename, destination, progBar);
				}
				
				
			}else if(arg0.getSource().equals(deleteSelectionMenuBar) || arg0.getSource().equals(deleteSelectionTable))
				deleteSelectedRows();
			else if(arg0.getSource().equals(moveSelectionUpMenuBar) || arg0.getSource().equals(moveSelectionUpTable))
				moveSelectedRowsUp();
			else if(arg0.getSource().equals(moveSelectionDownMenuBar) || arg0.getSource().equals(moveSelectionDownTable))
				moveSelectedRowsDown();
			else if(arg0.getSource().equals(duplicateSelectionMenuBar) || arg0.getSource().equals(duplicateSelectionTable))
				duplicateSelectedRows();
			else if(arg0.getSource().equals(undoDeletionMenubar) || arg0.getSource().equals(undoDeletionTable))
				undoPreviousDeletion();
		}	
	}
}
