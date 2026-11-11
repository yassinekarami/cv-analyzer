package org.cvanalyzer.backoffice.service;

import org.cvanalyzer.backoffice.model.CvMatcherDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CvImportService {

    String handleFileImport(MultipartFile file) throws IOException;
}
