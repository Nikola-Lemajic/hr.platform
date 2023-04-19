package com.hr.platform.support;

import com.hr.platform.dto.JobCandidateDto;
import com.hr.platform.model.JobCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JobCandidateDtoToJobCandidate implements Converter<JobCandidateDto, JobCandidate> {

    private final SkillDtoToSkill toSkill;

    @Autowired
    public JobCandidateDtoToJobCandidate(SkillDtoToSkill toSkill) {
        this.toSkill = toSkill;
    }

    @Override
    public JobCandidate convert(JobCandidateDto jobCandidateDto) {

        JobCandidate jobCandidate = new JobCandidate();

        jobCandidate.setName(jobCandidateDto.getName());
        jobCandidate.setDateOfBirth(jobCandidateDto.getDateOfBirth());
        jobCandidate.setContactNumber(jobCandidateDto.getContactNumber());
        jobCandidate.setEmail(jobCandidateDto.getEmail());
        jobCandidate.setSkills(toSkill.convert(jobCandidateDto.getSkills()));

        return jobCandidate;
    }
}
