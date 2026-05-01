package org.cvanalyzer.backoffice.repository;

import org.apache.ibatis.annotations.*;

import org.cvanalyzer.backoffice.model.EmbeddedCvDto;

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
    List<EmbeddedCvDto> findByFilename(@Param("filesname") List<String> filesname);

}