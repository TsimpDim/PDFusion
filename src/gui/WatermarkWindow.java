package gui;

import javax.swing.*;
import java.awt.*;

public class WatermarkWindow extends JFrame {

    private JPanel container;
    private JLabel wtrmkTextLabel;
    private JTextField wtrmkTextField;
    private JLabel wtrmkPositionLabel;
    private JComboBox wtrmkPositionDropdown;
    private String[] wtrmkPositions = {"Top Left", "Top Right", "Top Center", "Bottom Left", "Bottom Right", "Bottom Center", "Center"};
    private JLabel wtrmkRotationLabel;
    private JSpinner wtrmkRotationSpinner;
    private SpinnerModel wtrmkRotationSpinnerModel;

    // Watermark opacity

    public WatermarkWindow(){

        container = new JPanel();
        wtrmkTextLabel = new JLabel("Watermark text:");
        wtrmkTextLabel.setAlignmentX(LEFT_ALIGNMENT);

        wtrmkTextField = new JTextField(20);
        wtrmkTextField.setMaximumSize(new Dimension(350, 30));
        wtrmkTextField.setAlignmentX(LEFT_ALIGNMENT);

        wtrmkPositionLabel = new JLabel("Watermark Position:");
        wtrmkPositionLabel.setAlignmentX(LEFT_ALIGNMENT);

        wtrmkPositionDropdown = new JComboBox(wtrmkPositions);
        wtrmkPositionDropdown.setMaximumSize(new Dimension(300,30));
        wtrmkPositionDropdown.setAlignmentX(LEFT_ALIGNMENT);
        wtrmkPositionDropdown.setSelectedIndex(3);

        wtrmkRotationLabel = new JLabel("Watermark Rotation (Degrees):");
        wtrmkRotationLabel.setAlignmentX(LEFT_ALIGNMENT);

        wtrmkRotationSpinnerModel = new SpinnerNumberModel(0, -360, 360, 1);
        wtrmkRotationSpinner = new JSpinner(wtrmkRotationSpinnerModel);
        wtrmkRotationSpinner.setMaximumSize(new Dimension(100, 30));
        wtrmkRotationSpinner.setAlignmentX(LEFT_ALIGNMENT);


        container.add(wtrmkTextLabel);
        container.add(wtrmkTextField);
        container.add(Box.createVerticalStrut(10));
        container.add(wtrmkPositionLabel);
        container.add(wtrmkPositionDropdown);
        container.add(Box.createVerticalStrut(10));
        container.add(wtrmkRotationLabel);
        container.add(wtrmkRotationSpinner);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));


        this.setContentPane(container);
        this.setTitle("PDFusion Stamper");
        this.setSize(400,500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
