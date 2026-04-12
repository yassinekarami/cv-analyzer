package org.cvanalyzer.backoffice;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;

public class DocumentService {

    public void detect(String inputPath) {
        Tika tika = new Tika();
        String mediaType = tika.detect(inputPath);


    }

}
