package PDF;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StudentaiPDFGenerator {

    public static void generatePDF(String filePath) {
        try {
            // Sukuriame PDF dokumentą
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Pridedame tekstą
            document.add(new Paragraph("Studentų informacija"));
            document.add(new Paragraph("Vardas: Jonas"));
            document.add(new Paragraph("Pavardė: Kazlauskas"));
            document.add(new Paragraph("Pažymiai: 9, 10, 8"));

            // Uždaryti dokumentą
            document.close();
            System.out.println("PDF sėkmingai sukurtas: " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Klaida kuriant PDF: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        generatePDF("studentai.pdf");
    }
}
