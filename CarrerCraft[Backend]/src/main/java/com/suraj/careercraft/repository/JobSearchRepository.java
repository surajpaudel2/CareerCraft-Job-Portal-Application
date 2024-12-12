package com.suraj.careercraft.repository;

import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.elasticsearch.JobDocument;

import java.util.List;

public interface JobSearchRepository {
    void save(JobDocument jobDocument);
    void deleteById(String id);

    List<JobDocument> search(JobSearchRequestDto requestDto);
}
