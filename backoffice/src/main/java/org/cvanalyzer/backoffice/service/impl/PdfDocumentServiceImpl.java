package org.cvanalyzer.backoffice.service.impl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.cvanalyzer.backoffice.service.DocumentService;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return text;
    }

}
