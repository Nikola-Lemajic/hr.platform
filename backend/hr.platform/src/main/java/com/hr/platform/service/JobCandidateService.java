package com.hr.platform.service;

import com.hr.platform.dto.JobCandidateDto;
import com.hr.platform.model.JobCandidate;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface JobCandidateService {

    Optional<JobCandidate> findOne(Long id);

    Page<JobCandidate> findAllByName(String name, Integer pageNo);

    JobCandidate save(JobCandidate jobCandidate);

    JobCandidate update(JobCandidateDto jobCandidateDto);

    void delete(Long id);

    Page<JobCandidate> search(List<Long> skillsIds, Integer pageNo);

    Page<JobCandidate> getAll(Integer pageNo);

    boolean existsById(Long id);
}
