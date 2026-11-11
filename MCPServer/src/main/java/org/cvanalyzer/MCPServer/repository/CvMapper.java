package org.cvanalyzer.MCPServer.repository;

import org.apache.ibatis.annotations.*;
import org.cvanalyzer.MCPServer.record.EmbeddedCvRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface CvMapper {

    @Select("""
        <script>
        SELECT 
            metadata->>'filename' AS filename,
            content,
            metadata
            FROM vector_store
            WHERE metadata->>'filename' IN
                <foreach item="item" collection="filesname" open="(" separator="," close=")">
                    #{item}
                </foreach>
        </script>
    """)
    @MapKey("filename")
    @Results({
            @Result(property = "filename", column = "filename"),
            @Result(property = "content", column = "content"),
            @Result(property = "metadata", column = "metadata")}
    )
    List<EmbeddedCvRecord> findByFilename(@Param("filesname") List<String> filesname);
}
