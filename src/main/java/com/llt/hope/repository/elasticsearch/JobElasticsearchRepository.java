package com.llt.hope.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.document.elasticsearch.JobDocument;

@Repository
public interface JobElasticsearchRepository extends ElasticsearchRepository<JobDocument, String> {
    Page<JobDocument> findByTitleContainingOrDescriptionContainingOrRequirementsContainingOrResponsibilitiesContaining(
            String title, String description, String requirements, String responsibilities, Pageable pageable);
}
