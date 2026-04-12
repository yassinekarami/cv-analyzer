package org.cvanalyzer.backoffice.service.impl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfDocumentServiceImpl implements DocumentService {



    public String extractContentFromFile(String inputPath)
            throws IOException {

        PDDocument document = Loader.loadPDF(
                new RandomAccessReadBufferedFile(inputPath));

        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }


}
