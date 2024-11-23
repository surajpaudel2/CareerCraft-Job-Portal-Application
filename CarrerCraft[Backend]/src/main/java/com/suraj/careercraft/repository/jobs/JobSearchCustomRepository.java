package com.suraj.careercraft.repository.jobs;


import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.elasticsearch.JobDocument;

import java.util.List;

public interface JobSearchCustomRepository {
    List<JobDocument> searchJobsDynamically(JobSearchRequestDto jobSearchRequestDto);
}
