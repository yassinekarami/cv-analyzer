package org.cvanalyzer.MCPServer.repository;

import org.apache.ibatis.annotations.*;
import org.cvanalyzer.MCPServer.record.EmbeddedCvRecord;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CvMapper {


    List<EmbeddedCvRecord> findByFilename(@Param("filenames") List<String> filenames);



}
