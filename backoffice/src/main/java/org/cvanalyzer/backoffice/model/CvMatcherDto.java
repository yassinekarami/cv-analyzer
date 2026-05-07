package org.cvanalyzer.backoffice.model;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class CvMatcherDto {

    private String filename;

    private String details;
}
