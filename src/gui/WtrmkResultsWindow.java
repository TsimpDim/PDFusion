package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WtrmkResultsWindow extends JFrame{


    private static final long serialVersionUID = -6484367016184781887L;

    JPanel container;
    JPanel progBarContainer;
    JPanel optionsContainer;
    JLabel label;
    JProgressBar progBar;
    JButton continueButton;

    int min = 0;
    int max = 0;

    public WtrmkResultsWindow(int max, String labelStr) {
        this.max = max;

        container = new JPanel();
        container.setLayout(new BorderLayout());

        progBarContainer = new JPanel();
        optionsContainer = new JPanel();
        progBar = new JProgressBar(min, max);
        label = new JLabel(labelStr);


        continueButton = new JButton("Continue");
        continueButton.setEnabled(false);
        continueButton.setFocusable(false);
        continueButton.addActionListener(e -> dispose());

        progBarContainer.add(label);
        progBarContainer.add(progBar);


        container.add(progBarContainer, BorderLayout.PAGE_START);
        container.add(optionsContainer, BorderLayout.CENTER);
        container.add(continueButton, BorderLayout.SOUTH);



        this.setTitle("PDFusion - Results...");

        ArrayList<Image> icons = new ArrayList<>();
        icons.add(new ImageIcon(getClass().getResource("/res/logo/PDFusion_logo_16.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/res/logo/PDFusion_logo_20.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/res/logo/PDFusion_logo_32.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/res/logo/PDFusion_logo_40.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/res/logo/PDFusion_logo_64.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/res/logo/PDFusion_logo_128.png")).getImage());
        this.setIconImages(icons);

        this.setContentPane(container);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400,150);
    }

    public void incrementValue() {
        progBar.setValue(progBar.getValue() + 1);

        if(progBar.getValue() == max) {

            continueButton.setEnabled(true);
            continueButton.setFocusable(true);
        }
    }

    public void changeLabel(String newString) {
        label.setText(newString);
    }
}
