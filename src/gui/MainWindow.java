package gui;

import javax.swing.*;

public class MainWindow extends JFrame{

	JPanel container;
	
	public MainWindow() {
		container = new JPanel();
		
		// Set content pane
		this.setContentPane(container);
		
		this.setTitle("Fusion Workspace");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,500);
	}
	
}

