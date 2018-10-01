package gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ResultsWindow extends JFrame{


	private static final long serialVersionUID = 5403992280435836358L;
	
	JPanel container;
	JPanel progBarContainer;
	JPanel optionsContainer;
	JLabel label;
	JProgressBar progBar;
	JCheckBox openMergedFileCB;
	JCheckBox openMergedFileDirectoryCB;
	JButton continueButton;
	
	String filename;
	String destination;
	
	int min = 0;
	int max = 0;
	
	public ResultsWindow(int max, String labelStr,  String destination) {
		this.max = max;
		this.destination = destination;
		
		ButtonListener buttonListener = new ButtonListener();
		
		container = new JPanel();
		container.setLayout(new BorderLayout());
		
		progBarContainer = new JPanel();
		optionsContainer = new JPanel();
		progBar = new JProgressBar(min, max);
		label = new JLabel(labelStr);
		
		openMergedFileCB = new JCheckBox("Open merged file");
		openMergedFileCB.setFocusable(false);
		openMergedFileCB.setEnabled(false);
		
		openMergedFileDirectoryCB = new JCheckBox("Open merged file directory");
		openMergedFileDirectoryCB.setFocusable(false);
		openMergedFileDirectoryCB.setEnabled(false);
		
		continueButton = new JButton("Continue");
		continueButton.setEnabled(false);
		continueButton.setFocusable(false);
		continueButton.addActionListener(buttonListener);
		
		progBarContainer.add(label);
		progBarContainer.add(progBar);
		
		optionsContainer.add(openMergedFileCB);
		optionsContainer.add(openMergedFileDirectoryCB);
		
		container.add(progBarContainer, BorderLayout.PAGE_START);
		container.add(optionsContainer, BorderLayout.CENTER);
		container.add(continueButton, BorderLayout.SOUTH);

		
		
		this.setTitle("PDFusion - Results...");

		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_16.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_20.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_32.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_40.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_64.png")).getImage());
		icons.add(new ImageIcon(getClass().getResource("/res/PDFusion_logo_128.png")).getImage());
		this.setIconImages(icons);

		this.setContentPane(container);
		this.setVisible(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,150);
	}
	
	public void incrementValue() {
		progBar.setValue(progBar.getValue() + 1);
		
		if(progBar.getValue() == max) {
			openMergedFileCB.setFocusable(true);
			openMergedFileCB.setEnabled(true);
			
			openMergedFileDirectoryCB.setFocusable(true);
			openMergedFileDirectoryCB.setEnabled(true);
			
			continueButton.setEnabled(true);
			continueButton.setFocusable(true);
		}
	}
	
	public void changeLabel(String newString) {
		label.setText(newString);
	}
	
	public void showBar() {
		this.setVisible(true);
	}
	
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource().equals(continueButton)) {
				
				if(openMergedFileCB.isSelected()) {
					try {
						Desktop.getDesktop().open(new File(destination));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Something went wrong when opening the file", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
				
				if(openMergedFileDirectoryCB.isSelected()) {
					try {
						Desktop.getDesktop().open(new File(destination.replace(filename, "")));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Something went wrong when opening the directory", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
				
				dispose();
			}
		}
	
	}
}
