package org.cvanalyzer.backoffice.repository;

import org.cvanalyzer.backoffice.model.CvDto;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CvRepository extends JpaRepository<CvDto, Long> {

    @Query(value = """
        SELECT * FROM documents
        WHERE metadata->>'filename' = :filename
        LIMIT 1
    """, nativeQuery = true)
    Optional<CvDto> findByFilename(@Param("filename") String filename);
}