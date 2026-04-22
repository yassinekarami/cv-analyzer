package org.cvanalyzer.backoffice.service.impl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PdfDocumentServiceImpl implements DocumentService {


    /**
     * extract the content of a file as a string
     * @param file the file to upload
     * @return a string representing the content of the input
     * @throws IOException IOExeption
     */
    public String extractContentFromFile(MultipartFile file)
            throws IOException {

        PDDocument document = Loader.loadPDF(file.getBytes());

        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        
        return text.replaceAll("[^\\x20-\\x7E\\n]", " ")
                .replaceAll("\\s+", " ");

    }

}
