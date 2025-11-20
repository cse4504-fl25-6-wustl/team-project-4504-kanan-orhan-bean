package ArchDesign.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import ArchDesign.Main;
import ArchDesign.responses.CommandLineSerializer;
import ArchDesign.responses.ExtendedSerializer;
import ArchDesign.responses.Response;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainWindow {
    private static JFrame frame;
    private static JPanel fileSelectionPanel;
    private static JPanel resultPanel;
    // private static JPanel artPanel;
    // private static JButton artButton;
    private static JLabel artLabel;
    // private static JPanel clientPanel;
    // private static JButton clientButton;
    // private static JLabel clientLabel;
    private static JButton submitButton;
    private static final File[] selectedFiles = {null, null};
    private static final String cratePath = "input/Site_Requirements_crate.csv";
    private static final String noCratePath = "input/Site_Requirements.csv";
    private static final String errString = "error";
    

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
        fileSelectionPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // top, left, bottom, right padding

        JLabel filesLabel = new JLabel("Generate Packing Information:");
        
        JPanel artPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton artButton = new JButton("Select a line input file");
        artPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, artButton.getHeight()));
        artPanel.setMinimumSize(new Dimension(fileSelectionPanel.getWidth(), artButton.getHeight()));
        artPanel.setBorder(new EmptyBorder(30, 0, 20, 0));
        artLabel = new JLabel("no file selected");
        artPanel.add(artLabel);
        artPanel.add(artButton);

        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel clientDropdownLabel = new JLabel("Does the client accept crates?");
        String[] options = {"yes", "no"};
        JComboBox<String> clientDropdown = new JComboBox<>(options);
        clientPanel.add(clientDropdownLabel);
        clientPanel.add(clientDropdown);
        clientPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, clientDropdown.getHeight()));
        // clientPanel.setMinimumSize(new Dimension(fileSelectionPanel.getWidth(), clientDropdown.getHeight()));;
        clientPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setEnabled(false);
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.add(submitButton);
        submitPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, submitButton.getHeight()));
        submitPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        // Connect GUI to logic
        artButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            // String input = textField.getText();
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFiles[0] = fileChooser.getSelectedFile();
                artLabel.setText("Selected: " + selectedFiles[0].getName());
                submitButton.setEnabled(true);
            }
        });

        submitButton.addActionListener(e -> {
            if ((selectedFiles[0] != null)) {
                submitButton.setEnabled(false);
                submitButton.setText("loading");
                // accepts crates (yes)
                if (clientDropdown.getSelectedIndex() == 0) {
                    selectedFiles[1] = new File(cratePath);
                }
                else {
                    selectedFiles[1] = new File(noCratePath);
                }
                try {
                    Response response = Main.generateResponseForMain(selectedFiles[0].getAbsolutePath(), selectedFiles[1].getAbsolutePath());
                    CommandLineSerializer cli = new CommandLineSerializer(response);
                    ExtendedSerializer extendedCLI = new ExtendedSerializer(response);
                    showResultPanel(cli.getSummary(), extendedCLI.getSummary());
                }
                catch (NullPointerException err) {
                    showResultPanel(err.getMessage(), errString);
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
        fileSelectionPanel.add(Box.createVerticalGlue());
        fileSelectionPanel.add(submitPanel);
    }

    private static void showResultPanel(String basicOutput, String advancedOutput) {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // top, left, bottom, right padding

        JLabel resultLabel = new JLabel("Result:");
        JTextArea resultArea = new JTextArea(basicOutput);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JButton txtFileButton = new JButton("Generate .txt file");
        txtFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtFileButton.setAlignmentY(Component.TOP_ALIGNMENT);
        txtFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("Choose where to save the file");

            String fileName = "ArchDesign Shipping Information"; // could make dynamic later

            int userSelection = fileChooser.showSaveDialog(frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File targetFolder = fileChooser.getSelectedFile();
                File targetFile = new File(targetFolder, fileName);

                // check if file exists already
                if (targetFile.exists()) {
                    int choice = JOptionPane.showConfirmDialog(frame, "File already exists:\n" + targetFile.getName() + "\nDo you want to overwrite it?","Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                        if (choice != JOptionPane.YES_OPTION) {
                            return; // cancel save
                        }
                }

                try (FileWriter writer = new FileWriter(targetFile)) {
                    writer.write(advancedOutput);
                    JOptionPane.showMessageDialog(frame, "File saved successfully:\n" + targetFile.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        JButton advancedButton = new JButton("Show advanced info");
        final boolean[] isAdvancedDisplay = {false};
        advancedButton.addActionListener(e -> {
            if (isAdvancedDisplay[0]) {
                resultArea.setText(basicOutput);
                advancedButton.setText("Show advanced info");
                isAdvancedDisplay[0] = !isAdvancedDisplay[0];
            }
            else {
                resultArea.setText(advancedOutput);
                advancedButton.setText("Show basic info");
                isAdvancedDisplay[0] = !isAdvancedDisplay[0];
            }
        });

        JButton backButton = new JButton("Back");
        // backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        // backButton.setAlignmentY(Component.TOP_ALIGNMENT);
        backButton.addActionListener(e -> {
            resetFilePanel();
            frame.setContentPane(fileSelectionPanel);
            frame.revalidate();
            frame.repaint();
        });
        JPanel topHeaderRow = new JPanel(new BorderLayout());
        // prevent vertical stretching when added to BoxLayout
        topHeaderRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, backButton.getPreferredSize().height));
        topHeaderRow.setBorder(new EmptyBorder(10,15,20,15));
        topHeaderRow.add(backButton, BorderLayout.WEST);
        topHeaderRow.add(advancedButton, BorderLayout.EAST);
        
        if (advancedOutput == errString) {
            advancedButton.setEnabled(false);
            txtFileButton.setEnabled(false);
        }
        
        resultPanel.add(resultLabel);
        resultPanel.add(topHeaderRow);
        resultPanel.add(scrollPane);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(txtFileButton);
    }

    private static void resetFilePanel() {
        selectedFiles[0] = null;
        selectedFiles[1] = null;
        artLabel.setText("no file selected");
        // clientLabel.setText("no file selected");
        submitButton.setEnabled(false);
        submitButton.setText("submit");
    }

}
