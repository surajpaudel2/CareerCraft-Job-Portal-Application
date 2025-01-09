package com.suraj.careercraft.repository;

import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.elasticsearch.JobDocument;

import java.util.Map;

public interface JobSearchRepository {
    void save(JobDocument jobDocument);
    void deleteById(String id);

    Map<String, Object> search(JobSearchRequestDto requestDto);
}
