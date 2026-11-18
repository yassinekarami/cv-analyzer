package org.cvanalyzer.MCPServer.record;

/**
 * Record return by the matching cv tool
 * @param filename the filename retrieved from the metadata
 * @param name the name retrieved from the metadata
 * @param email the email retrieved from the metadata
 */
public record CvMatchingResponseRecord(
        String filename,
        String name,
        String email
) {

    /**
     * Add the filename to the CvMatchingResponseRecord
     * @param filename the filename
     * @return a cvMatchingResponse with the filename
     */
    public CvMatchingResponseRecord withFilename(String filename) {
        return new CvMatchingResponseRecord(filename, name, email);
    }
}
