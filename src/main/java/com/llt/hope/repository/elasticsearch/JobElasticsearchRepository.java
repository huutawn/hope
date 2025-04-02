package com.llt.hope.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.llt.hope.document.elasticsearch.JobDocument;

@Repository
public interface JobElasticsearchRepository extends ElasticsearchRepository<JobDocument, String> {
    @Query("{\"bool\": {\"should\": [" +
            "{\"match\": {\"title\": {\"query\": \"?0\", \"operator\": \"and\"}}}," +
            "{\"match\": {\"description\": {\"query\": \"?0\", \"operator\": \"and\"}}}" +
            "]}}")
    Page<JobDocument> searchJobs(String keyword, Pageable pageable);
}

