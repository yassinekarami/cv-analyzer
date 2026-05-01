package org.cvanalyzer.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public record CvDto(
        Profile profile,
        List<Experience> experience,
        List<Education> education,
        List<String> skills,
        List<Publication> publications,
        List<Talk> talks,
        List<Certification> certifications,
        List<Object> other
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize
    @JsonDeserialize
    public record Profile(
            String name,
            String email,
            String nationality,
            List<String> links,
            String title,
            Map<String, String> languages
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize
    @JsonDeserialize
    public record Experience(
            String role,
            String dates,
            String company,
            String location,
            String description
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize
    @JsonDeserialize
    public record Education(
            String degree,
            String school,
            String year
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize
    @JsonDeserialize
    public record Publication(
            String title,
            String publisher
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize
    @JsonDeserialize
    public record Talk(
            String title,
            String event,
            String location,
            String date
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize
    @JsonDeserialize
    public record Certification(
            String title,
            String description
    ) {}
}