package com.resumebuilder.gui;

import com.resumebuilder.dao.ResumeDAO;
import com.resumebuilder.model.Resume;
import com.resumebuilder.util.PdfGenerator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    private JPanel previewPanel;
    private JTabbedPane tabbedPane;
    private JTextArea summaryArea, skillsArea, previewArea;
    private JTextField jobTitleField, firstNameField, lastNameField, emailField, phoneField, countryField, cityField;
    private JTextField jobRoleField, companyField, startDateField, endDateField;
    private JTextArea responsibilitiesArea;
    private JTextField degreeField, universityField, graduationYearField;

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
        getContentPane().setBackground(new Color(245, 245, 245));

        // Create tabbed pane with custom styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 240, 255));
        tabbedPane.setForeground(Color.DARK_GRAY);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
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

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(51, 153, 255));
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
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        return button;
    }

    private void addPersonalDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 250, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        String[] labels = {"Job Title", "First Name", "Last Name", "Email", "Phone", "Country", "City"};
        JTextField[] fields = {
                jobTitleField = createTextField(),
                firstNameField = createTextField(),
                lastNameField = createTextField(),
                emailField = createTextField(),
                phoneField = createTextField(),
                countryField = createTextField(),
                cityField = createTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("SansSerif", Font.PLAIN, 16));
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2; // Expand input field to occupy more space
            panel.add(fields[i], gbc);
        }

        tabbedPane.addTab("Personal Details", panel);
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
        panel.setBackground(new Color(255, 240, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        degreeField = createTextField();
        universityField = createTextField();
        graduationYearField = createTextField();

        JTextField[] fields = {degreeField, universityField, graduationYearField};
        String[] labels = {"Degree", "University", "Graduation Year"};

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

        tabbedPane.addTab("Education", panel);
    }

    private void setupPreviewPanel() {
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(new Color(255, 255, 245));
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
                        "Education:\nDegree: %s\nUniversity: %s\nGraduation Year: %s",
                jobTitleField.getText(), firstNameField.getText(), lastNameField.getText(),
                emailField.getText(), phoneField.getText(), countryField.getText(), cityField.getText(),
                summaryArea.getText(), skillsArea.getText(),
                jobRoleField.getText(), companyField.getText(), startDateField.getText(), endDateField.getText(), responsibilitiesArea.getText(),
                degreeField.getText(), universityField.getText(), graduationYearField.getText()
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

        PdfGenerator pdfGenerator = new PdfGenerator();
        pdfGenerator.generatePdf(resume, "resume.pdf");

        JOptionPane.showMessageDialog(this, "PDF downloaded successfully.");
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
