package org.cvanalyzer.backoffice.model;

/**
 * record containing the information relative to the scheduled meeting
 * @param organizerEmail the organizer email
 * @param candidateEmail the candidate email
 * @param interviewDate the interview date
 * @param startTime the interview start time
 * @param endTime the interview end time
 * @param timeZone the timezone
 * @param bookingId the booking id
 * @param bookingStatus the booking status
 */
public record MeetingScheduleRecord(
        String organizerEmail,
        String candidateEmail,
        String interviewDate,
        String startTime,
        String endTime,
        String timeZone,
        String bookingId,
        String bookingStatus
) {
}
