package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.PdfFile;
import control.PdfWorkspace;

public class MainWindow extends JFrame{


	private static final long serialVersionUID = -6007278116878081383L;

	PdfWorkspace workspace = null;

	JPanel container;

	JFileChooser fileChooser;

	JTable fileTable;
	JScrollPane fileTablePane;
	PdfFileTableModel tableModel;
	JPopupMenu tableMenu;

	JMenuBar menuBar;
	JMenu selectionMenu;
	JMenu openMenu;
	JMenu editMenu;

	JMenuItem openFiles;
	JMenuItem mergeFiles;
	JMenuItem watermarkFiles;

	DeleteRowsAction deleteSelectedRowsAction = new DeleteRowsAction("Delete selected file(s)", null,null, null, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
	MoveRowsUpAction moveSelectionUpAction = new MoveRowsUpAction("Move file up", null, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK));
	MoveRowsDownAction moveSelectionDownAction = new MoveRowsDownAction("Move file down", null, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK));
	DuplicateRowsAction duplicateSelectionAction = new DuplicateRowsAction("Duplicate file(s)", null, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
	UndoDeletionAction undoDeletionAction = new UndoDeletionAction("Undo deletion", null, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
	OpenFileAction openFileAction = new OpenFileAction("Open selected file(s)", null, null);

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


		// Edit Menu
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		mergeFiles = new JMenuItem("Merge files");
		mergeFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.CTRL_MASK));

		watermarkFiles = new JMenuItem("Watermark files");

		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");
		fileChooser.setFileFilter(filter);

		mergeFiles.addActionListener(buttonListener);
		watermarkFiles.addActionListener(buttonListener);


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

		tableMenu.add(deleteSelectedRowsAction);
		tableMenu.add(moveSelectionUpAction);
		tableMenu.add(moveSelectionDownAction);
		tableMenu.add(duplicateSelectionAction);
		tableMenu.add(undoDeletionAction);
		tableMenu.add(new JSeparator());
		tableMenu.add(openFileAction);

		fileTable.setComponentPopupMenu(tableMenu);


	    // Component setup
		openMenu.add(openFiles);

		editMenu.add(mergeFiles);
		editMenu.add(watermarkFiles);

		selectionMenu.add(deleteSelectedRowsAction);
		selectionMenu.add(moveSelectionUpAction);
		selectionMenu.add(moveSelectionDownAction);
		selectionMenu.add(duplicateSelectionAction);
		selectionMenu.add(undoDeletionAction);

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

	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (arg0.getSource().equals(openFiles)) { // Open files and add them into the workspace

				int returnVal = fileChooser.showOpenDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] new_files = fileChooser.getSelectedFiles();


					for (File file : new_files) {

						String curPath = file.getPath();
						int fileIndex = PdfWorkspace.totalFiles;

						PdfFile newPDF = new PdfFile(curPath, true, fileIndex);
						workspace.addFileToWorkspace(newPDF);

					}
					tableModel.updateData(workspace.getAllFiles());
				}

			} else if (arg0.getSource().equals(mergeFiles)) { // Merge files

				fileChooser.setSelectedFile(new File("export.pdf")); // Sets default filename

				int returnVal = fileChooser.showSaveDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					String destination = fileChooser.getSelectedFile().getPath();

					if (!destination.endsWith(".pdf"))
						destination += ".pdf";


					// Initialize progress bar
					ResultsWindow progBar = new ResultsWindow(PdfWorkspace.totalFilesToInclude, "Preparing files...", destination);
					workspace.mergeFiles(destination, progBar);
				}
			} else if (arg0.getSource().equals(watermarkFiles)){ // Watermark files

				WatermarkWindow wtrmkWindow = new WatermarkWindow(workspace, fileTable.getSelectedRows());
			}
		}
	}

	/**
	 * Action for deleting selected rows from the workspace only if it is possible
	 */
	class DeleteRowsAction extends AbstractAction {

		public DeleteRowsAction(String text, ImageIcon icon,
						  String desc, Integer mnemonic, KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		public void actionPerformed(ActionEvent e){

			int selectedRow = fileTable.getSelectedRow();
			if(selectedRow > -1) {
				int[] selectedRows = fileTable.getSelectedRows();


				if(workspace.removeFilesFromWorkspace(selectedRows)) {
					tableModel.fireTableRowsDeleted(selectedRows[0], selectedRows[selectedRows.length - 1]);

					// If the deleted file was the only file then we have nothing to select
					if(workspace.totalFiles == 0)
						return;

					try{
						fileTable.setRowSelectionInterval(selectedRow, selectedRow);
					}catch(java.lang.IllegalArgumentException er){
						fileTable.setRowSelectionInterval(selectedRow -1, selectedRow -1);
					}
				}
			}
		}
	}

	/**
	 * Action for moving selected rows higher on the workspace only if it is possible
	 */
	class MoveRowsUpAction extends AbstractAction{

		public MoveRowsUpAction(String text, ImageIcon icon,
								String desc, Integer mnemonic, KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		public void actionPerformed(ActionEvent e){
			int selectedRow = fileTable.getSelectedRow();
			if(selectedRow > 0) {
				int[] selectedRows = fileTable.getSelectedRows();
				if(workspace.moveFilesUp(selectedRows)) {
					tableModel.fireTableDataChanged();
					fileTable.setRowSelectionInterval(selectedRows[0] - 1, selectedRows[selectedRows.length - 1] - 1); // Move selection upwards
				}
			}
		}
	}

	/**
	 * Action for moving selected rows lower on the workspace only if it is possible
	 */
	class MoveRowsDownAction extends AbstractAction{

		public MoveRowsDownAction(String text, ImageIcon icon,
								String desc, Integer mnemonic, KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		public void actionPerformed(ActionEvent e){
			int selectedRow = fileTable.getSelectedRow();
			if(selectedRow > -1) {
				int[] selectedRows = fileTable.getSelectedRows();
				if(workspace.moveFilesDown(selectedRows)) {
					tableModel.fireTableDataChanged();
					fileTable.setRowSelectionInterval(selectedRows[0] + 1, selectedRows[selectedRows.length - 1] + 1); // Move selection downwards
				}
			}
		}
	}

	/**
	 * Duplicates selected rows
	 */
	class DuplicateRowsAction extends AbstractAction{

		public DuplicateRowsAction(String text, ImageIcon icon,
								  String desc, Integer mnemonic, KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		public void actionPerformed(ActionEvent e){
			int [] selectedRows = fileTable.getSelectedRows();
			int rows = PdfWorkspace.totalFiles;

			workspace.duplicateFiles(selectedRows);

			tableModel.fireTableDataChanged();
			fileTable.setRowSelectionInterval(rows, rows + selectedRows.length - 1);
		}
	}

	/**
	 * Undoes the last deletion
	 */
	class UndoDeletionAction extends AbstractAction{

		public UndoDeletionAction(String text, ImageIcon icon,
								   String desc, Integer mnemonic, KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		public void actionPerformed(ActionEvent e){
			workspace.undoPreviousDeletion();
			tableModel.fireTableDataChanged();
		}
	}

	/**
	 * Opens all the selected files
	 */
	class OpenFileAction extends AbstractAction {

		public OpenFileAction(String text, ImageIcon icon, String desc) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
		}

		public void actionPerformed(ActionEvent e){

			int selectedRow = fileTable.getSelectedRow();
			if(selectedRow > -1) {
				int[] selectedRows = fileTable.getSelectedRows();

				for(int pageIdx : selectedRows){
					try{
						Desktop.getDesktop().open(new File(workspace.getFile(pageIdx).getPath()));
					}catch(java.io.IOException ex){
						JOptionPane.showMessageDialog(null, "Could not open file.", "Error!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		}
	}
}
