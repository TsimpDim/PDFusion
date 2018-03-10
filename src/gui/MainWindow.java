package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.PdfFile;
import control.PdfWorkspace;

public class MainWindow extends JFrame{

	PdfWorkspace workspace = null;
	
	JPanel container;
	
	JPanel sidePanel;
	JButton openFilesButton;
	JFileChooser fileChooser;
	
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
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");
		fileChooser.setFileFilter(filter);
		
	    openFilesButton.addActionListener(buttonListener);
	    
	    // Component setup
	    sidePanel.add(openFilesButton);
	    container.add(sidePanel);
	    
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
					
					String curPath = fileChooser.getSelectedFile().getPath();
					int fileIndex = PdfWorkspace.totalFiles++;
					
					PdfFile newPDF = new PdfFile(curPath, true, fileIndex);
					workspace.AddPdfToWorkspace(newPDF);
					System.out.println(workspace.toString());
				}
			
			}
		}
	}
}

