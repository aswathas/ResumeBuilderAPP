package com.resumebuilder.gui;

import com.resumebuilder.dao.ResumeDAO;
import com.resumebuilder.model.Resume;
import com.resumebuilder.util.PdfGenerator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;



public class MainWindow extends JFrame {
    private JPanel previewPanel;
    private JTabbedPane tabbedPane;
    private JTextArea summaryArea, skillsArea, previewArea;
    private JTextField jobTitleField, firstNameField, lastNameField, emailField, phoneField, countryField, cityField;
    private JTextField jobRoleField, companyField, startDateField, endDateField;
    private JTextArea responsibilitiesArea;
    private JTextField degreeField, universityField, graduationYearField, tenthField, twelfthField;
    private JLabel profileImageLabel;
    private String profileImagePath;
    public MainWindow() {
        setTitle("Resume Builder");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel settings
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background
        setLayout(new BorderLayout(10, 10));

        // Add title
        JLabel titleLabel = new JLabel("Resume Builder", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(34, 139, 34)); // Green text
        add(titleLabel, BorderLayout.NORTH);

        // Create tabbed pane with custom styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 248, 255)); // Light blue background
        tabbedPane.setForeground(new Color(34, 139, 34)); // Green text
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tabbedPane, BorderLayout.CENTER);

        // Add tabs
        addPersonalDetailsTab();
        addProfessionalSummaryTab();
        addSkillsTab();
        addProfessionalExperienceTab();
        addEducationTab();

        // Setup menu bar
        setupMenuBar();

        // Add preview panel
        setupPreviewPanel();

        pack();
        setVisible(true);

        // Initial preview update
        updateResumePreview();
    }

    // private void setupMenuBar() {
    //     JMenuBar menuBar = new JMenuBar();
    //     menuBar.setBackground(new Color(51, 153, 255));
    //     setJMenuBar(menuBar);

    //     JButton downloadPdfButton = createStyledButton("Download PDF");
    //     downloadPdfButton.addActionListener(e -> downloadPdf());
    //     menuBar.add(downloadPdfButton);

    //     JButton saveButton = createStyledButton("Save Resume");
    //     saveButton.addActionListener(e -> saveResume());
    //     menuBar.add(saveButton);
    // }

    // private JButton createStyledButton(String text) {
    //     JButton button = new JButton(text);
    //     button.setBackground(new Color(0, 122, 204));
    //     button.setForeground(Color.WHITE);
    //     button.setFont(new Font("SansSerif", Font.BOLD, 12));
    //     button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    //     button.setFocusPainted(false);
    //     return button;
    // }
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(34, 139, 34)); // Green background
        setJMenuBar(menuBar);

        JButton downloadPdfButton = createStyledButton("Download PDF");
        downloadPdfButton.addActionListener(e -> downloadPdf());
        menuBar.add(downloadPdfButton);

        JButton saveButton = createStyledButton("Save Resume");
        saveButton.addActionListener(e -> saveResume());
        menuBar.add(saveButton);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(34, 139, 34)); // Green background
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        return button;
    }

    // private void addPersonalDetailsTab() {
    //     JPanel panel = new JPanel(new GridBagLayout());
    //     panel.setBackground(new Color(240, 255, 255));
    //     GridBagConstraints gbc = new GridBagConstraints();
    //     gbc.insets = new Insets(10, 10, 10, 10);
    //     gbc.fill = GridBagConstraints.HORIZONTAL;
    //     gbc.weightx = 1.0;

    //     jobTitleField = createTextField();
    //     firstNameField = createTextField();
    //     lastNameField = createTextField();
    //     emailField = createTextField();
    //     phoneField = createTextField();
    //     countryField = createTextField();
    //     cityField = createTextField();

    //     JTextField[] fields = {jobTitleField, firstNameField, lastNameField, emailField, phoneField, countryField, cityField};
    //     String[] labels = {"Job Title", "First Name", "Last Name", "Email", "Phone", "Country", "City"};

    //     for (int i = 0; i < labels.length; i++) {
    //         gbc.gridx = 0;
    //         gbc.gridy = i;
    //         panel.add(new JLabel(labels[i]), gbc);

    //         gbc.gridx = 1;
    //         panel.add(fields[i], gbc);
    //     }

    //     // Add profile image upload button and label
    //     profileImageLabel = new JLabel();
    //     profileImageLabel.setPreferredSize(new Dimension(150, 150));
    //     profileImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    //     gbc.gridx = 0;
    //     gbc.gridy = labels.length;
    //     gbc.gridwidth = 2;
    //     panel.add(profileImageLabel, gbc);

    //     JButton uploadButton = new JButton("Upload Photo");
    //     uploadButton.addActionListener(e -> uploadProfileImage());
    //     gbc.gridy = labels.length + 1;
    //     panel.add(uploadButton, gbc);

    //     tabbedPane.addTab("Personal Details", panel);
    
    // }
    private void addPersonalDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Reduced insets to move labels closer to text fields
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        jobTitleField = createTextField();
        firstNameField = createTextField();
        lastNameField = createTextField();
        emailField = createTextField();
        phoneField = createTextField();
        countryField = createTextField();
        cityField = createTextField();

        JTextField[] fields = {jobTitleField, firstNameField, lastNameField, emailField, phoneField, countryField, cityField};
        String[] labels = {"Job Title", "First Name", "Last Name", "Email", "Phone", "Country", "City"};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel label = new JLabel(labels[i]);
            label.setForeground(new Color(34, 139, 34)); // Green text
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST; // Align text fields to the left
            panel.add(fields[i], gbc);
        }

        // Add profile image upload button and label
        profileImageLabel = new JLabel();
        profileImageLabel.setPreferredSize(new Dimension(150, 150));
        profileImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(profileImageLabel, gbc);

        JButton uploadButton = new JButton("Upload Photo");
        uploadButton.setBackground(new Color(34, 139, 34)); // Green background
        uploadButton.setForeground(Color.WHITE);
        uploadButton.addActionListener(e -> uploadProfileImage());
        gbc.gridy = labels.length + 1;
        panel.add(uploadButton, gbc);

        tabbedPane.addTab("Personal Details", panel);
    }
   private void uploadProfileImage() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select Profile Image");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

    // Optional: Set the initial directory to user's home for better accessibility
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

    int userSelection = fileChooser.showOpenDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        // Validate the selected file is an image
        if (!isImageFile(selectedFile)) {
            JOptionPane.showMessageDialog(this, "Please select a valid image file.", 
                "Invalid File", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Store the absolute path of the selected image
            profileImagePath = selectedFile.getAbsolutePath();

            // Read and scale the image for display in the GUI
            BufferedImage img = ImageIO.read(selectedFile);
            Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            profileImageLabel.setIcon(new ImageIcon(scaledImg));

            // Update preview if necessary
            updateResumePreview();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            profileImagePath = null;
            profileImageLabel.setIcon(null);
        }
    }
}

/**
 * Helper method to validate if the selected file is an image based on its extension.
 *
 * @param file The file to validate.
 * @return True if the file is an image; false otherwise.
 */
private boolean isImageFile(File file) {
    String[] acceptedExtensions = {"jpg", "jpeg", "png", "gif"};
    String fileName = file.getName().toLowerCase();
    for (String ext : acceptedExtensions) {
        if (fileName.endsWith("." + ext)) {
            return true;
        }
    }
    return false;
}
    


    private void addProfessionalSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 255, 255));
        summaryArea = new JTextArea(5, 30);
        summaryArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        summaryArea.getDocument().addDocumentListener(new PreviewDocumentListener());
        panel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        tabbedPane.addTab("Professional Summary", panel);
    }

    private void addSkillsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 255));
        skillsArea = new JTextArea(5, 30);
        skillsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        skillsArea.getDocument().addDocumentListener(new PreviewDocumentListener());
        panel.add(new JScrollPane(skillsArea), BorderLayout.CENTER);
        tabbedPane.addTab("Skills", panel);
    }

    private void addProfessionalExperienceTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(250, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        jobRoleField = createTextField();
        companyField = createTextField();
        startDateField = createTextField();
        endDateField = createTextField();
        responsibilitiesArea = new JTextArea(5, 20);
        responsibilitiesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        responsibilitiesArea.getDocument().addDocumentListener(new PreviewDocumentListener());

        JTextField[] fields = {jobRoleField, companyField, startDateField, endDateField};
        String[] labels = {"Job Role", "Company", "Start Date", "End Date"};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("SansSerif", Font.PLAIN, 16));
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            panel.add(fields[i], gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 1;
        JLabel responsibilitiesLabel = new JLabel("Responsibilities");
        responsibilitiesLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(responsibilitiesLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(responsibilitiesArea), gbc);

        tabbedPane.addTab("Professional Experience", panel);
    }

    private void addEducationTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
    
        degreeField = createTextField();
        universityField = createTextField();
        graduationYearField = createTextField();
        tenthField = createTextField();
        twelfthField = createTextField();
    
        JTextField[] fields = {degreeField, universityField, graduationYearField, tenthField, twelfthField};
        String[] labels = {"Degree", "University", "Graduation Year", "10th Marks", "12th Marks"};
    
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
    
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }
    
        tabbedPane.addTab("Education", panel);
    }

    // private void setupPreviewPanel() {
    //     previewPanel = new JPanel(new BorderLayout());
    //     previewPanel.setBackground(new Color(255, 255, 245));
    //     previewPanel.setBorder(BorderFactory.createTitledBorder("Resume Preview"));

    //     previewArea = new JTextArea();
    //     previewArea.setEditable(false);
    //     previewArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
    //     previewPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);

    //     JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, previewPanel);
    //     splitPane.setDividerLocation(900); // Adjusted divider for better layout
    //     splitPane.setResizeWeight(0.6); // Adjust space allocation
    //     splitPane.setOneTouchExpandable(true);

    //     add(splitPane, BorderLayout.CENTER);
    // }
    private void setupPreviewPanel() {
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        previewPanel.setBorder(BorderFactory.createTitledBorder("Resume Preview"));

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        previewPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, previewPanel);
        splitPane.setDividerLocation(900); // Adjusted divider for better layout
        splitPane.setResizeWeight(0.6); // Adjust space allocation
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);
    }
    private void updateResumePreview() {
        String previewText = String.format(
                "Job Title: %s\nFirst Name: %s\nLast Name: %s\nEmail: %s\nPhone: %s\nCountry: %s\nCity: %s\n\n" +
                        "Professional Summary:\n%s\n\nSkills:\n%s\n\n" +
                        "Professional Experience:\nJob Role: %s\nCompany: %s\nStart Date: %s\nEnd Date: %s\nResponsibilities: %s\n\n" +
                        "Education:\nDegree: %s\nUniversity: %s\nGraduation Year: %s\n10th Marks: %s\n12th Marks: %s",
                jobTitleField.getText(), firstNameField.getText(), lastNameField.getText(),
                emailField.getText(), phoneField.getText(), countryField.getText(), cityField.getText(),
                summaryArea.getText(), skillsArea.getText(),
                jobRoleField.getText(), companyField.getText(), startDateField.getText(), endDateField.getText(), responsibilitiesArea.getText(),
                degreeField.getText(), universityField.getText(), graduationYearField.getText(), tenthField.getText(), twelfthField.getText()
        );

        previewArea.setText(previewText);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.getDocument().addDocumentListener(new PreviewDocumentListener());
        return textField;
    }

    private class PreviewDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) { updateResumePreview(); }
        public void removeUpdate(DocumentEvent e) { updateResumePreview(); }
        public void changedUpdate(DocumentEvent e) { updateResumePreview(); }
    }


    private void downloadPdf() {
        Resume resume = new Resume();
        resume.setJobTitle(jobTitleField.getText());
        resume.setFirstName(firstNameField.getText());
        resume.setLastName(lastNameField.getText());
        resume.setEmail(emailField.getText());
        resume.setPhone(phoneField.getText());
        resume.setCountry(countryField.getText());
        resume.setCity(cityField.getText());
        resume.setProfessionalSummary(summaryArea.getText());
        resume.setSkills(skillsArea.getText());

        resume.setJobRole(jobRoleField.getText());
        resume.setCompany(companyField.getText());
        resume.setStartDate(startDateField.getText());
        resume.setEndDate(endDateField.getText());
        resume.setResponsibilities(responsibilitiesArea.getText());

        resume.setDegree(degreeField.getText());
        resume.setUniversity(universityField.getText());
        resume.setGraduationYear(graduationYearField.getText());
        resume.setTenthMark(tenthField.getText());
        resume.setTwelfthMark(twelfthField.getText());

        // Verify image path is still valid before setting it
        if (profileImagePath != null && new File(profileImagePath).exists()) {
            resume.setProfileImagePath(profileImagePath);
        } else {
            resume.setProfileImagePath(null); // Don't use invalid path
        }
        
        // Let user choose where to save the PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
        fileChooser.setSelectedFile(new File("resume.pdf"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            PdfGenerator pdfGenerator = new PdfGenerator();
            pdfGenerator.generatePdf(resume, filePath);
            
            JOptionPane.showMessageDialog(this, "PDF downloaded successfully.");
        }
    }

    private void saveResume() {
        Resume resume = new Resume();

        resume.setJobTitle(jobTitleField.getText());
        resume.setFirstName(firstNameField.getText());
        resume.setLastName(lastNameField.getText());
        resume.setEmail(emailField.getText());
        resume.setPhone(phoneField.getText());
        resume.setCountry(countryField.getText());
        resume.setCity(cityField.getText());
        resume.setProfessionalSummary(summaryArea.getText());
        resume.setSkills(skillsArea.getText());

        resume.setJobRole(jobRoleField.getText());
        resume.setCompany(companyField.getText());
        resume.setStartDate(startDateField.getText());
        resume.setEndDate(endDateField.getText());
        resume.setResponsibilities(responsibilitiesArea.getText());

        resume.setDegree(degreeField.getText());
        resume.setUniversity(universityField.getText());
        resume.setGraduationYear(graduationYearField.getText());
        resume.setTenthMark(tenthField.getText());
        resume.setTwelfthMark(twelfthField.getText());
    
        
        // Set the profile image path
        if (profileImagePath != null && new File(profileImagePath).exists()) {
            resume.setProfileImagePath(profileImagePath);
        }

        ResumeDAO dao = new ResumeDAO();
        try {
            dao.saveResume(resume);
            JOptionPane.showMessageDialog(this, "Resume saved successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to save resume: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
