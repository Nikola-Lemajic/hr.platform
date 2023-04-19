package com.hr.platform.support;

import com.hr.platform.dto.JobCandidateDto;
import com.hr.platform.model.JobCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobCandidateToJobCandidateDto implements Converter<JobCandidate, JobCandidateDto> {

    private final SkillToSkillDto toSkillDto;

    @Autowired
    public JobCandidateToJobCandidateDto(SkillToSkillDto toSkillDto) {
        this.toSkillDto = toSkillDto;
    }

    @Override
    public JobCandidateDto convert(JobCandidate jobCandidate) {

        JobCandidateDto jobCandidateDto = new JobCandidateDto();

        jobCandidateDto.setId(jobCandidate.getId());
        jobCandidateDto.setName(jobCandidate.getName());
        jobCandidateDto.setDateOfBirth(jobCandidate.getDateOfBirth());
        jobCandidateDto.setContactNumber(jobCandidate.getContactNumber());
        jobCandidateDto.setEmail(jobCandidate.getEmail());
        jobCandidateDto.setSkills(toSkillDto.convert(jobCandidate.getSkills()));

        return jobCandidateDto;
    }

    public List<JobCandidateDto> convert(List<JobCandidate> jobCandidates) {

        return jobCandidates.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
