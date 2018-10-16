package gui;

import control.PdfWorkspace;
import control.WatermarkOptions;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WatermarkWindow extends JFrame {

    private JPanel container;
    private JPanel insetSubContainer;
    private JLabel wtrmkTextLabel;
    private JTextField wtrmkTextField;
    private JLabel wtrmkPositionLabel;
    private JComboBox wtrmkPositionDropdown;
    private String[] wtrmkPositions = {"Top Left", "Top Right", "Top Center", "Bottom Left", "Bottom Right", "Bottom Center", "Center"};
    private JLabel wtrmkRotationLabel;
    private JSpinner wtrmkRotationSpinner;
    private SpinnerModel wtrmkRotationSpinnerModel;
    private JLabel wtrmkOpacityLabel;
    private JSlider wtrmkOpacitySlider;
    private JRadioButton wtrmkSelectedFilesRadioBut;
    private JRadioButton wtrmkAllFilesRadioBut;
    private ButtonGroup wtrmkFileChoiceButtonGroup;
    private JButton watermarkButton;

    public WatermarkWindow(PdfWorkspace workspace, int[] selectedRows){

        container = new JPanel();
        insetSubContainer = new JPanel();

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

        wtrmkOpacityLabel = new JLabel("Watermark Opacity:");
        wtrmkOpacitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        wtrmkOpacitySlider.setMaximumSize(new Dimension(250, 40));
        wtrmkOpacitySlider.setAlignmentX(LEFT_ALIGNMENT);

        wtrmkSelectedFilesRadioBut = new JRadioButton("Watermark selected files");
        wtrmkAllFilesRadioBut = new JRadioButton("Watermark all files");
        wtrmkFileChoiceButtonGroup = new ButtonGroup();
        wtrmkFileChoiceButtonGroup.add(wtrmkSelectedFilesRadioBut);
        wtrmkFileChoiceButtonGroup.add(wtrmkAllFilesRadioBut);
        wtrmkSelectedFilesRadioBut.setSelected(true);

        watermarkButton = new JButton("Watermark");
        watermarkButton.setMaximumSize(new Dimension(500, 100));
        watermarkButton.setFont(new Font("Arial", Font.PLAIN, 25));
        watermarkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wtrmkText = wtrmkTextField.getText();

                if(wtrmkText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Watermark text cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Integer wtrmkPos = wtrmkPositionDropdown.getSelectedIndex();
                Integer wtrmkRot = (Integer) wtrmkRotationSpinner.getValue();
                Integer wtrmkOpac = wtrmkOpacitySlider.getValue();
                Boolean wtrmkAllFiles = false;

                if(wtrmkAllFilesRadioBut.isSelected())
                    wtrmkAllFiles = true;

                if(!wtrmkAllFiles && selectedRows.length == 0){ // Wtrmk selected files but no files are selected
                    JOptionPane.showMessageDialog(null, "No files/rows are selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // !!! Despite wtrmkOpac being an Integer it is saved as a float for easier use !!!
                // Division by 100 is done to convert Opacity to a range from 0-0.1 instead of 0-100 (i.e a percentage)
                WatermarkOptions wtrmkOptions = new WatermarkOptions(selectedRows, wtrmkText, wtrmkPos, wtrmkRot, (float)wtrmkOpac/100, wtrmkAllFiles);
                workspace.watermarkFiles(wtrmkOptions);

                setVisible(false);
                dispose();
            }
        });

        insetSubContainer.add(Box.createVerticalStrut(20));
        insetSubContainer.add(wtrmkTextLabel);
        insetSubContainer.add(wtrmkTextField);
        insetSubContainer.add(Box.createVerticalStrut(10));
        insetSubContainer.add(wtrmkPositionLabel);
        insetSubContainer.add(wtrmkPositionDropdown);
        insetSubContainer.add(Box.createVerticalStrut(10));
        insetSubContainer.add(wtrmkRotationLabel);
        insetSubContainer.add(wtrmkRotationSpinner);
        insetSubContainer.add(Box.createVerticalStrut(10));
        insetSubContainer.add(wtrmkOpacityLabel);
        insetSubContainer.add(wtrmkOpacitySlider);
        insetSubContainer.add(Box.createVerticalStrut(50));
        insetSubContainer.add(wtrmkSelectedFilesRadioBut);
        insetSubContainer.add(wtrmkAllFilesRadioBut);
        insetSubContainer.add(Box.createVerticalStrut(50));

        insetSubContainer.setLayout(new BoxLayout(insetSubContainer, BoxLayout.Y_AXIS));
        insetSubContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(insetSubContainer);
        container.add(Box.createHorizontalGlue());
        container.add(watermarkButton);

        this.setContentPane(container);
        this.setTitle("PDFusion Stamper");
        this.setSize(400,500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
    }
}
