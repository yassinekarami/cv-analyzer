package org.cvanalyzer.backoffice.model;

/**
 * record containing data to be stored as metadata in neo4j graph
 * @param details details
 * @param email the email figuring in the found cv
 * @param filename the filename of the found cv
 * @param name the name figuring in the found cv
 */
public record UserMetadataRecord (
        String details,
        String email,
        String filename,
        String name){
}
