package org.cvanalyzer.backoffice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class EmbeddedCvDto {

    private String filename;

    private String id;

    private String content;

    private String metadata;

    private String embedding;


    /**
     * Constructor for EmbeddedCv
     * @param filename the embedded filename
     * @param metadata the embeddedCv metadata
     * @param content the embeddedCv content
     */
    public EmbeddedCvDto(String filename, String metadata, String content) {
        this.filename = filename;
        this.metadata = metadata;
        this.content = content;
    }
}
