package org.cvanalyzer.backoffice.service;

import java.io.IOException;

public interface DocumentService {

    String extractContentFromFile(String input) throws IOException;
}
