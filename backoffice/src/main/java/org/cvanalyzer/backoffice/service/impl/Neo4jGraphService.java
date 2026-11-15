package org.cvanalyzer.backoffice.service.impl;


import org.cvanalyzer.backoffice.service.KnowledgeGraphService;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Neo4jGraphService implements KnowledgeGraphService {

    /**
     * neo4j driver used to execute queries
     */
    @Autowired
    private Driver neo4jDriver;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> retreiveRelatedSkills(String sessionId) {

        Map<String, Object> parameters = Map.of(
                "sessionId", sessionId
        );

        String query = """
                MATCH (s:Session {id: $sessionId})
                -[:RELATED_TO]-(related:Skill)
                 RETURN DISTINCT related.name AS skill
                """;
        return neo4jDriver.executableQuery(query)
                .withParameters(parameters)
                .execute()
                .records()
                .stream()
                .map(record -> record.get("skill").asString())
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNodeInKnowledgeGraph(String sessionId, String skill) {

        String query = """
            MERGE (s:Session {id: $sessionId})
            MERGE (sk:Skill {name: $skill})
            MERGE (s)-[:RELATED_TO]->(sk)
            
            """;

        Map<String, Object> parameters = Map.of(
                "sessionId", sessionId,
                "skill", skill
        );

        var result = neo4jDriver.executableQuery(query)
                .withParameters(parameters)
                .execute();

        var counters = result.summary().counters();
        System.out.println("sessionId : " +sessionId);
        System.out.println("Relations créées : " + counters.relationshipsCreated());
        System.out.println("Relations supprimées : " + counters.relationshipsDeleted());
        System.out.println("Nœuds supprimés : " + counters.nodesDeleted());
    }
}
