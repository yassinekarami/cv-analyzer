package org.cvanalyzer.MCPServer.record;

/**
 * the embedded generate by the vector database
 * @param filename
 * @param id
 * @param content
 * @param metadata
 * @param embedding
 */
public record EmbeddedCvRecord(
        String filename,
        String id,
        String content,
        String metadata,
        String embedding) {
}
