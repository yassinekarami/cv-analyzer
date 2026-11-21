package org.cvanalyzer.backoffice.service;

import org.cvanalyzer.backoffice.model.CvMatcherRecord;
import org.cvanalyzer.backoffice.model.MeetingScheduleRecord;

public interface CvAnalysisService {

    CvMatcherRecord searchCvBasedOnCriteria(String criteria);


    MeetingScheduleRecord scheduleMeeting();
}
