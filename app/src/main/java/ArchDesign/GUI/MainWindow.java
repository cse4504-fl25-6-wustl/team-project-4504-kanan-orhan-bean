package ArchDesign.GUI;

import javax.swing.*;

import ArchDesign.Main;
import ArchDesign.responses.CommandLineSerializer;
import ArchDesign.responses.Response;

import java.awt.*;
import java.io.File;

public class MainWindow {
    private static JFrame frame;
    private static JPanel fileSelectionPanel;
    private static JPanel resultPanel;
    // private static JPanel artPanel;
    // private static JButton artButton;
    private static JLabel artLabel;
    // private static JPanel clientPanel;
    // private static JButton clientButton;
    private static JLabel clientLabel;
    private static JButton submitButton;
    private static final File[] selectedFiles = {null, null};
    

    public static void run(){
        SwingUtilities.invokeLater(MainWindow::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("ArchDesign App");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());

        setupFileSelectionPanel();
        frame.setContentPane(fileSelectionPanel);
        frame.setVisible(true);
    }

    private static void setupFileSelectionPanel() {
        // Main panel with vertical layout
        fileSelectionPanel = new JPanel();
        fileSelectionPanel.setLayout(new BoxLayout(fileSelectionPanel, BoxLayout.Y_AXIS));

        JLabel filesLabel = new JLabel("Generate Packing Information:");
        JPanel artPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton artButton = new JButton("Select a line input file");
        artLabel = new JLabel("no file selected");
        artPanel.add(artLabel);
        artPanel.add(artButton);

        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton clientButton = new JButton("Select a client input file");
        clientLabel = new JLabel("no file selected");
        clientPanel.add(clientLabel);
        clientPanel.add(clientButton);
        
        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setEnabled(false);
        
        // Connect GUI to logic
        artButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            // String input = textField.getText();
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFiles[0] = fileChooser.getSelectedFile();
                artLabel.setText("Selected: " + selectedFiles[0].getName());
                if (selectedFiles[1] != null) {
                    submitButton.setEnabled(true);
                }
            }
        });
        clientButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            // String input = textField.getText();
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFiles[1] = fileChooser.getSelectedFile();
                clientLabel.setText("Selected: " + selectedFiles[1].getName());
                if (selectedFiles[0] != null) {
                    submitButton.setEnabled(true);
                }
            }
        });

        submitButton.addActionListener(e -> {
            if ((selectedFiles[0] != null) && (selectedFiles[1] != null)) {
                // String output = "congratulations!!! you have submitted: \n Line Item Input: " + selectedFiles[0].getName() +
                // "\n Client Input: " + selectedFiles[1].getName();
                // selectedFiles[0].getAbsolutePath()
                // You can now pass selectedFile[0] to your Controller logic
                try {
                    Response response = Main.generateResponseForMain(selectedFiles[0].getAbsolutePath(), selectedFiles[1].getAbsolutePath());
                    CommandLineSerializer cli = new CommandLineSerializer(response);
                    showResultPanel(cli.getSummary());
                }
                catch (NullPointerException err) {
                    showResultPanel(err.getMessage());
                }
                
                frame.setContentPane(resultPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        // add companents to main panel
        fileSelectionPanel.add(filesLabel);
        fileSelectionPanel.add(artPanel);
        fileSelectionPanel.add(clientPanel);
        fileSelectionPanel.add(submitButton);
    }

    private static void showResultPanel(String output) {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel resultLabel = new JLabel("Result:");
        JTextArea resultArea = new JTextArea(output);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            resetFilePanel();
            frame.setContentPane(fileSelectionPanel);
            frame.revalidate();
            frame.repaint();
        });

        resultPanel.add(resultLabel);
        resultPanel.add(scrollPane);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(backButton);
    }

    private static void resetFilePanel() {
        selectedFiles[0] = null;
        selectedFiles[1] = null;
        artLabel.setText("no file selected");
        clientLabel.setText("no file selected");
        submitButton.setEnabled(false);
    }

}
