
package com.example.LearningPlatformApp.service;

import com.example.LearningPlatformApp.entity.Certificate;
import org.openpdf.text.Chunk;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Service
public class CertificatePdfService {

    private static final Color NAVY = new Color(20, 43, 76);
    private static final Color GOLD = new Color(190, 150, 65);

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);

    public String generateCertificate(Certificate certificate) {

        Path directory = Path.of("uploads", "certificates");

        String fileName = UUID.randomUUID() + ".pdf";
        Path filePath = directory.resolve(fileName);

        try {
            Files.createDirectories(directory);

            try (OutputStream outputStream = Files.newOutputStream(filePath)) {

                Document document = new Document(
                        PageSize.A4.rotate(), 40, 40, 35, 35
                );

                try {
                    PdfWriter.getInstance(document, outputStream);
                    document.open();

                    // Certificate title
                    Font titleFont = new Font(
                            Font.HELVETICA, 30, Font.BOLD, NAVY
                    );

                    Paragraph title = new Paragraph(
                            "CERTIFICATE OF COMPLETION",
                            titleFont
                    );

                    title.setAlignment(Element.ALIGN_CENTER);
                    title.setSpacingAfter(25);
                    document.add(title);

                    // Introductory text
                    addCenteredText(
                            document,
                            "This is to certify that",
                            16,
                            Font.NORMAL,
                            Color.DARK_GRAY
                    );

                    // Student name
                    String studentName =
                            certificate.getStudent().getFirstName()
                                    + " "
                                    + certificate.getStudent().getLastName();

                    addCenteredText(
                            document,
                            studentName,
                            27,
                            Font.BOLD,
                            NAVY
                    );

                    // Completion statement
                    addCenteredText(
                            document,
                            "has successfully completed the course",
                            16,
                            Font.NORMAL,
                            Color.DARK_GRAY
                    );

                    // Course title
                    addCenteredText(
                            document,
                            certificate.getCourse().getTitle(),
                            23,
                            Font.BOLD,
                            GOLD
                    );

                    document.add(Chunk.NEWLINE);

                    // Certificate details
                    addCenteredText(
                            document,
                            "Certificate Number: "
                                    + certificate.getCertificateNumber(),
                            11,
                            Font.NORMAL,
                            NAVY
                    );

                    addCenteredText(
                            document,
                            "Issued On: "
                                    + certificate.getIssuedAt()
                                    .format(DATE_FORMAT),
                            11,
                            Font.NORMAL,
                            NAVY
                    );

                    document.add(Chunk.NEWLINE);

                    addCenteredText(
                            document,
                            "LearningPlatform",
                            14,
                            Font.BOLD,
                            NAVY
                    );

                } finally {
                    if (document.isOpen()) {
                        document.close();
                    }
                }
            }

            return filePath.toString();

        } catch (IOException | DocumentException exception) {

            try {
                Files.deleteIfExists(filePath);
            } catch (IOException cleanupException) {
                exception.addSuppressed(cleanupException);
            }

            throw new IllegalStateException(
                    "Failed to generate certificate PDF",
                    exception
            );
        }
    }

    private void addCenteredText(
            Document document,
            String text,
            float fontSize,
            int fontStyle,
            Color color
    ) throws DocumentException {

        Font font = new Font(
                Font.HELVETICA,
                fontSize,
                fontStyle,
                color
        );

        Paragraph paragraph = new Paragraph(
                new Phrase(text, font)
        );

        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(12);

        document.add(paragraph);
    }
}
