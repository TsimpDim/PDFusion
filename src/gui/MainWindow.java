package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
	JFileChooser fileChooser;
	
	JTable fileTable;
	JScrollPane fileTablePane;
	PdfFileTableModel tableModel;
    String[] tableColumnNames = {"id","path","include","pages"};
    
	
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
		
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");
		fileChooser.setFileFilter(filter);
		
	    openFilesButton.addActionListener(buttonListener);
	    
	    // Table
	    fileTable = new JTable();
	    tableModel = new PdfFileTableModel(null);
	    fileTable.setFillsViewportHeight(true);
	    fileTable.setModel(tableModel);
	    fileTable.getColumnModel().getColumn(0).setMaxWidth(20);
	    fileTable.getColumnModel().getColumn(2).setMaxWidth(60);
	    fileTable.getColumnModel().getColumn(3).setMaxWidth(60);

	    
	    fileTablePane = new JScrollPane(fileTable);
	    
	    // Component setup
	    sidePanel.add(openFilesButton);
	    
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
						int fileIndex = PdfWorkspace.totalFiles++;
						
						PdfFile newPDF = new PdfFile(curPath, true, fileIndex);
						workspace.AddPdfToWorkspace(newPDF);
					}
					System.out.println(workspace.toString());
					tableModel.updateData(workspace.getAllFiles());
				}
			
			}
		}
	}
}

