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
}
