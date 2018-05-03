package gui;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ResultsWindow extends JFrame{


	private static final long serialVersionUID = 5403992280435836358L;
	
	JPanel container;
	JLabel label;
	JProgressBar progBar;
	
	
	int min = 0;
	int max = 0;
	
	public ResultsWindow(int max, String labelStr) {
		this.max = max;
		
		container = new JPanel();
		progBar = new JProgressBar(min, max);
		label = new JLabel(labelStr);
		
		container.add(label);
		container.add(progBar);
		
		this.setTitle("PDFusion - Working...");

		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon("./res/PDFusion_logo_16.png").getImage());
		icons.add(new ImageIcon("./res/PDFusion_logo_20.png").getImage());
		icons.add(new ImageIcon("./res/PDFusion_logo_32.png").getImage());
		icons.add(new ImageIcon("./res/PDFusion_logo_40.png").getImage());
		icons.add(new ImageIcon("./res/PDFusion_logo_64.png").getImage());
		icons.add(new ImageIcon("./res/PDFusion_logo_128.png").getImage());
		this.setIconImages(icons);

		this.setContentPane(container);
		this.setVisible(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,150);
	}
	
	public void incrementValue() {
		progBar.setValue(progBar.getValue() + 1);
	}
	
	public void changeLabel(String newString) {
		label.setText(newString);
	}
	
	public void showBar(boolean arg) {
		this.setVisible(arg);
	}
}
