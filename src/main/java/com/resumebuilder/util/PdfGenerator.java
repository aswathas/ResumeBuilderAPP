package com.resumebuilder.util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.resumebuilder.model.Resume;
import com.itextpdf.io.image.ImageData;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PdfGenerator {

        public void generatePdf(Resume resume, String filePath) {
                try {
                        // Initialize PDF writer and document
                        PdfWriter writer = new PdfWriter(filePath);
                        PdfDocument pdfDoc = new PdfDocument(writer);
                        Document document = new Document(pdfDoc, PageSize.A4);

                        // Set font styles
                        PdfFont headerFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
                        PdfFont bodyFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

                        // 1. Add header section (Name, Contact Information, and Image)
                        addHeader(document, resume, resume.getProfileImagePath(), headerFont, bodyFont);

                        // Add extra spacing before adding the line
                        document.add(new Paragraph("\n\n"));  // Adds spacing to avoid overlap with the image

                        // Add line separator
                        document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(20));

                        // 2. Professional Summary Section
                        document.add(new Paragraph("Professional Summary")
                                        .setFont(headerFont)
                                        .setFontSize(16)
                                        .setMarginBottom(10));
                        document.add(new Paragraph(safeString(resume.getProfessionalSummary()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setMarginBottom(20));

                        // Add line separator
                        document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

                        // 3. Skills Section
                        document.add(new Paragraph("Skills")
                                        .setFont(headerFont)
                                        .setFontSize(16)
                                        .setMarginBottom(10));
                        document.add(new Paragraph(safeString(resume.getSkills()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setMarginBottom(20));

                        // Add line separator
                        document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

                        // 4. Professional Experience Section
                        document.add(new Paragraph("Professional Experience")
                                        .setFont(headerFont)
                                        .setFontSize(16)
                                        .setMarginBottom(10));
                        document.add(new Paragraph(safeString(resume.getJobRole()) + " | " + safeString(resume.getCompany()))
                                        .setFont(headerFont)
                                        .setFontSize(14)
                                        .setMarginBottom(5));
                        document.add(new Paragraph(safeString(resume.getStartDate()) + " - " + safeString(resume.getEndDate()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setItalic()
                                        .setMarginBottom(10));
                        document.add(new Paragraph("Responsibilities:")
                                        .setFont(headerFont)
                                        .setFontSize(12)
                                        .setMarginBottom(5));
                        document.add(new Paragraph(safeString(resume.getResponsibilities()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setMarginBottom(20));

                        // Add line separator
                        document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

                        // 5. Education Section
                        document.add(new Paragraph("Education")
                                        .setFont(headerFont)
                                        .setFontSize(16)
                                        .setMarginBottom(10));
                        document.add(new Paragraph(safeString(resume.getDegree()))
                                        .setFont(headerFont)
                                        .setFontSize(14)
                                        .setMarginBottom(5));
                        document.add(new Paragraph(safeString(resume.getUniversity()) + " | Graduated: " + safeString(resume.getGraduationYear()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setMarginBottom(5));
                        document.add(new Paragraph("10th Marks: " + safeString(resume.getTenthMark()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setMarginBottom(5));
                        document.add(new Paragraph("12th Marks: " + safeString(resume.getTwelfthMark()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setMarginBottom(20));

                        // Close the document
                        document.close();
                        System.out.println("PDF generated successfully!");

                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        // Helper method to handle null strings
        private String safeString(String value) {
                return value == null ? "" : value;
        }

        // Method to add the header (name, contact info, and image) with proper alignment
        private void addHeader(Document document, Resume resume, String imagePath, PdfFont headerFont, PdfFont bodyFont) {
                try {
                        // Add image first if available to ensure it's at the topmost layer
                        if (imagePath != null && !imagePath.trim().isEmpty()) {
                                try {
                                        ImageData imageData = ImageDataFactory.create(imagePath);
                                        Image img = new Image(imageData);

                                        // Scale image maintaining aspect ratio
                                        float maxWidth = 100;
                                        float maxHeight = 100;
                                        float aspectRatio = img.getImageWidth() / img.getImageHeight();

                                        if (aspectRatio > 1) {
                                                img.setWidth(maxWidth);
                                                img.setHeight(maxWidth / aspectRatio);
                                        } else {
                                                img.setHeight(maxHeight);
                                                img.setWidth(maxHeight * aspectRatio);
                                        }

                                        // Position image in top-right corner
                                        float pageWidth = document.getPdfDocument().getDefaultPageSize().getWidth();
                                        float pageHeight = document.getPdfDocument().getDefaultPageSize().getHeight();
                                        
                                        // Set smaller margins for tighter positioning
                                        float marginRight = 20;
                                        float marginTop = 20;

                                        img.setFixedPosition(
                                                pageWidth - img.getImageScaledWidth() - marginRight,
                                                pageHeight - img.getImageScaledHeight() - marginTop
                                        );

                                        document.add(img);
                                } catch (Exception e) {
                                        System.err.println("Error adding profile image: " + e.getMessage());
                                }
                        }

                        // Add text content with appropriate spacing to avoid overlap with image
                        Paragraph header = new Paragraph(safeString(resume.getFirstName()) + " " + safeString(resume.getLastName()))
                                .setFont(headerFont)
                                .setFontSize(22)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginBottom(5)
                                .setMarginTop(20); // Add top margin to align with image

                        document.add(header);

                        Paragraph contactInfo = new Paragraph(safeString(resume.getEmail()) + " | " + safeString(resume.getPhone()) + " | " +
                                        safeString(resume.getCity()) + ", " + safeString(resume.getCountry()))
                                        .setFont(bodyFont)
                                        .setFontSize(12)
                                        .setTextAlignment(TextAlignment.LEFT)
                                        .setMarginBottom(10);
                        document.add(contactInfo);

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
