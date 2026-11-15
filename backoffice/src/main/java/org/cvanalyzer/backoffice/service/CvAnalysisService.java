package org.cvanalyzer.backoffice.service;

import org.cvanalyzer.backoffice.model.CvMatcherRecord;

public interface CvAnalysisService {

    CvMatcherRecord searchCvBasedOnCriteria(String criteria);
}
