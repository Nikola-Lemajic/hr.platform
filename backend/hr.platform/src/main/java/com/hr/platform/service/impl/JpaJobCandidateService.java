package com.hr.platform.service.impl;

import com.hr.platform.dto.JobCandidateDto;
import com.hr.platform.model.JobCandidate;
import com.hr.platform.repository.JobCandidateRepository;
import com.hr.platform.service.JobCandidateService;
import com.hr.platform.support.SkillDtoToSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaJobCandidateService implements JobCandidateService {

    private final JobCandidateRepository jobCandidateRepository;

    private final SkillDtoToSkill toSkill;

    @Autowired
    public JpaJobCandidateService(
            JobCandidateRepository jobCandidateRepository,
            SkillDtoToSkill toSkill
    ) {
        this.jobCandidateRepository = jobCandidateRepository;
        this.toSkill = toSkill;
    }

    @Override
    public Optional<JobCandidate> findOne(Long id) {
        return jobCandidateRepository.findById(id);
    }

    @Override
    public Page<JobCandidate> findAllByName(String name, Integer pageNo) {
        return jobCandidateRepository.findByNameContainsIgnoreCase(name, PageRequest.of(pageNo, 5));
    }

    @Override
    public JobCandidate save(JobCandidate jobCandidate) {
        return jobCandidateRepository.save(jobCandidate);
    }

    @Override
    public JobCandidate update(JobCandidateDto jobCandidateDto) {

        return jobCandidateRepository.findById(jobCandidateDto.getId())
                .map(jobCandidate -> {
                    jobCandidate.setName(jobCandidateDto.getName());
                    jobCandidate.setDateOfBirth(jobCandidateDto.getDateOfBirth());
                    jobCandidate.setContactNumber(jobCandidateDto.getContactNumber());
                    jobCandidate.setEmail(jobCandidateDto.getEmail());
                    jobCandidate.setSkills(toSkill.convert(jobCandidateDto.getSkills()));
                    return jobCandidateRepository.save(jobCandidate);
                }).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        jobCandidateRepository.deleteById(id);
    }

    @Override
    public Page<JobCandidate> search(List<Long> skillsIds, Integer pageNo) {
        return jobCandidateRepository.findBySkillsId(skillsIds, skillsIds.size(), PageRequest.of(pageNo, 5));
    }

    @Override
    public Page<JobCandidate> getAll(Integer pageNo) {
        return jobCandidateRepository.findAll(PageRequest.of(pageNo, 5));
    }

    @Override
    public boolean existsById(Long id) {
        return jobCandidateRepository.existsById(id);
    }

}
