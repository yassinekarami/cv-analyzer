package org.cvanalyzer.MCPServer.record;

/**
 * the embedded generate by the vector database
 * @param filename
 * @param metadata
 */
public record EmbeddedCvRecord(
        String filename,
        String content,
        String metadata) {
}
