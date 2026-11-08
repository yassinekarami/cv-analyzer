package org.cvanalyzer.MCPServer.record;

/**
 * The cv score calculate by the tool
 * @param filename
 * @param overralScore
 */
public record CvScoreResponseRecord(
        String filename,
        String overralScore) {
}
