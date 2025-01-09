package com.suraj.careercraft.controller;

import com.suraj.careercraft.dto.request.JobRequestDto;
import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.dto.response.JobResponseDto;
import com.suraj.careercraft.exceptions.NoJobFoundException;
import com.suraj.careercraft.exceptions.UnauthorizedAccessException;
import com.suraj.careercraft.model.Job;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.service.BeanValidationService;
import com.suraj.careercraft.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job")
public class JobController {
    private final Validator validator;
    private final BeanValidationService beanValidationService;
    private final JobService jobService;

    @Autowired
    public JobController(Validator validator, BeanValidationService beanValidationService, JobService jobService) {
        this.validator = validator;
        this.beanValidationService = beanValidationService;
        this.jobService = jobService;
    }

    //    ---------------------Creation Of Job -----------------------------

    /*
      Things that are done in this method :
          i. First done with validation from the validation services. (BeanValidationService), if there is
          validation error then sending the generated validation error messages to the client.

          ii. If our validation satisfies then creating the Job object with the help of JobRequestDto that we get
          from the user.

          iii. Saving/Creating th job after above 2 points success.

          iv. Sending Response
   */
    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody JobRequestDto jobRequestDto) {
        if (jobRequestDto.getId() != null) {
            throw new UnauthorizedAccessException("Unauthorized attempt to create the job");
        }

        BindingResult bindingResult = new BeanPropertyBindingResult(jobRequestDto, "job");
        validator.validate(jobRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String messages = beanValidationService.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest().body(messages);
        }

        Job job = jobService.convertDto(jobRequestDto);
        jobService.createJob(job);

        JobResponseDto jobResponseDto = new JobResponseDto(true, "Job created successfully");

        return new ResponseEntity<>(jobResponseDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateJob(@RequestBody JobRequestDto jobRequestDto) {
        if (jobRequestDto.getId() == null) {
            throw new NoJobFoundException("No Such Job found");
        }

        BindingResult bindingResult = new BeanPropertyBindingResult(jobRequestDto, "job");
        validator.validate(jobRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            String messages = beanValidationService.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest().body(messages);
        }

        Job job = jobService.convertDto(jobRequestDto);
        jobService.createJob(job);

        JobResponseDto jobResponseDto = new JobResponseDto(true, "Job updated successfully");

        return new ResponseEntity<>(jobResponseDto, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteJob(@RequestParam long id) {
        System.out.println(id + "  Here comes the id");
        jobService.deleteJob(id);
        JobResponseDto jobResponseDto = new JobResponseDto(true, "Job deleted successfully");
        return new ResponseEntity<>(jobResponseDto, HttpStatus.OK);
    }


    @PostMapping("/search")
    public ResponseEntity<?> searchJob(@RequestBody JobSearchRequestDto searchRequestDto) {
        System.out.println(searchRequestDto.getSize() + " " + searchRequestDto.getPage() + " ");
        Map<String, Object> map = jobService.searchJobs(searchRequestDto);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
