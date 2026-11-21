package org.cvanalyzer.backoffice.service;

import org.cvanalyzer.backoffice.model.UserMetadataRecord;

import java.util.List;

public interface KnowledgeGraphService {

    /**
     * retrieve the skills related to the skill given in the parameters
     * @param sessionId the graph session id
     * @return list of skills as string, related to the skill in the sessionId
     */
    List<String> retreiveRelatedSkills(String sessionId);

    /**
     * add a new node to the graph with the given sessionId
     * @param sessionId sessionId the graph session id
     * @param skill skill the skill which we want to retrieve related skill
     */
    void addNodeInKnowledgeGraph(String sessionId, String skill);


    /**
     * retrieve the email and user related to a session
     * @param sessionId the session id
     */
    UserMetadataRecord findUserAndEmail(String sessionId);
}
