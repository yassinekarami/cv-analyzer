package org.cvanalyzer.backoffice.service;

import org.cvanalyzer.backoffice.model.CvMatcherDto;

public interface CvAnalysisService {

    CvMatcherDto searchCvBasedOnCriteria(String criteria);
}
