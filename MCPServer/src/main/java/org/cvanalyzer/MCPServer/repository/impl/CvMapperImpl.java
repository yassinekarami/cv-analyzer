package org.cvanalyzer.MCPServer.repository.impl;

import org.apache.ibatis.annotations.Mapper;
import org.cvanalyzer.MCPServer.record.EmbeddedCvRecord;
import org.cvanalyzer.MCPServer.repository.CvMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Component
public class CvMapperImpl implements CvMapper {

    private final JdbcTemplate jdbcTemplate ;

    public CvMapperImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<EmbeddedCvRecord> findByFilename(List<String> filenames) {

        if (filenames == null || filenames.isEmpty()) {
            return List.of();
        }

        String sql = """
                SELECT
                    metadata->>'filename' AS filename,
                    content,
                    metadata
                FROM importedcvvs
                WHERE metadata->>'filename' IN (%s) AND metadata ->>'categorie' = 'profile'
                """.formatted(
                String.join(",", Collections.nCopies(filenames.size(), "?"))
        );

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new EmbeddedCvRecord(
                                rs.getString("filename"),
                                rs.getString("content"),
                                rs.getString("metadata")
                        ),
                filenames.toArray()
        );
    }
}
