package org.cvanalyzer.backoffice.service.impl;


import org.cvanalyzer.backoffice.model.UserMetadataRecord;
import org.cvanalyzer.backoffice.service.KnowledgeGraphService;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.neo4j.driver.internal.value.NodeValue;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public UserMetadataRecord findUserAndEmail(String sessionId) {


        String query = """
                MATCH (m:Metadata)
                WHERE m.sessionId = $sessionId
                RETURN m
                """
                ;

        Map<String, Object> parameters = Map.of(
                "sessionId", sessionId
        );

        List<Record> records = neo4jDriver.executableQuery(query)
                .withParameters(parameters)
                .execute()
                .records();

        return build(records.getFirst().values().getFirst());
    }

    /**
     * create a UserMetadataRecord
     * @param value the value from wich the userMetadata in created
     * @return new UserMeatadata record
     */
    private UserMetadataRecord build(Value value) {
        return UserMetadataRecord.builder()
                .email(value.get("email").asString())
                .filename(value.get("filename").asString())
                .name(value.get("name").asString())
                .details(value.get("details").asString())
                .build();
    }
}
