package com.hr.platform.service.impl;

import com.hr.platform.dto.JobCandidateDto;
import com.hr.platform.dto.SkillDto;
import com.hr.platform.model.JobCandidate;
import com.hr.platform.model.Skill;
import com.hr.platform.repository.JobCandidateRepository;
import com.hr.platform.service.JobCandidateService;
import com.hr.platform.support.SkillDtoToSkill;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class JpaJobCandidateServiceTest {

    private JobCandidateRepository jobCandidateRepository;
    private SkillDtoToSkill toSkill;
    private JobCandidateService jobCandidateService;

    @BeforeEach
    void setup() {
        jobCandidateRepository = Mockito.mock(JobCandidateRepository.class);
        toSkill = new SkillDtoToSkill();
        jobCandidateService = new JpaJobCandidateService(jobCandidateRepository, toSkill);
    }

    private JobCandidate getJobCandidate() {
        return getJobCandidate(
                1L,
                "Test",
                "069101101",
                "test@email.com",
                LocalDate.of(2000, 4, 12),
                getSkills()
        );
    }

    private static List<Skill> getSkills() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1L, "TestSkill"));
        skills.add(new Skill(2L, "TestSkill2"));
        return skills;
    }

    private JobCandidate getJobCandidate(
            Long id,
            String name,
            String contactNumber,
            String email,
            LocalDate dateOfBirth,
            List<Skill> skills
    ) {
        JobCandidate jobCandidate = new JobCandidate();
        jobCandidate.setId(id);
        jobCandidate.setName(name);
        jobCandidate.setContactNumber(contactNumber);
        jobCandidate.setEmail(email);
        jobCandidate.setDateOfBirth(dateOfBirth);
        jobCandidate.setSkills(skills);

        return jobCandidate;
    }

    private List<JobCandidate> getJobCandidateList() {

        JobCandidate jobCandidate = getJobCandidate(
                1L,
                "Test",
                "069101101",
                "test@email.com",
                LocalDate.of(2000, 4, 12),
                getSkills()
        );

        JobCandidate jobCandidate2 = getJobCandidate(
                2L,
                "Test2",
                "069109101",
                "tes2t@email.com",
                LocalDate.of(2001, 4, 12),
                getSkills()
        );

        List<JobCandidate> jobCandidateList = new ArrayList<>();

        jobCandidateList.add(jobCandidate);
        jobCandidateList.add(jobCandidate2);

        return jobCandidateList;
    }

    private List<Long> getSkillIds() {

        List<Long> skillIds = new ArrayList<>();

        skillIds.add(1L);
        skillIds.add(2L);

        return skillIds;
    }

    private JobCandidateDto getJobCandidateDto() {

        JobCandidateDto jobCandidateDto = new JobCandidateDto();
        List<SkillDto> skillDtos = new ArrayList<>();
        SkillDto skillDto = new SkillDto();
        skillDto.setName("test skill");
        skillDtos.add(skillDto);

        jobCandidateDto.setId(1L);
        jobCandidateDto.setName("Test");
        jobCandidateDto.setEmail("test@email.com");
        jobCandidateDto.setContactNumber("069101101");
        jobCandidateDto.setDateOfBirth(LocalDate.of(2000, 4, 12));
        jobCandidateDto.setSkills(skillDtos);

        return jobCandidateDto;
    }

    @Test
    void when_findOne_returnSuccess() {
        JobCandidate jobCandidate = getJobCandidate();
        Long id = 1L;

        when(jobCandidateRepository.findById(id)).thenReturn(Optional.of(jobCandidate));

        Optional<JobCandidate> response = jobCandidateService.findOne(id);

        assertEquals(response.get().getId(), jobCandidate.getId());
    }

    @Test
    void when_findAllByName_returnSuccess() {
        List<JobCandidate> jobCandidatesList = getJobCandidateList();
        Page<JobCandidate> jobCandidatePage = new PageImpl<>(jobCandidatesList);
        String name = "Test";
        Integer pageNo = 1;

        when(jobCandidateRepository.findByNameContainsIgnoreCase(name, PageRequest.of(pageNo, 5))).thenReturn(jobCandidatePage);

        Page<JobCandidate> response = jobCandidateService.findAllByName(name, pageNo);

        assertEquals(response.getContent().size(), jobCandidatePage.getContent().size());
    }

    @Test
    void when_save_returnSuccess() {
        JobCandidate jobCandidateExpected = getJobCandidate();
        JobCandidate jobCandidateReturned = getJobCandidate();

        when(jobCandidateRepository.save(jobCandidateExpected)).thenReturn(jobCandidateReturned);

        JobCandidate response = jobCandidateService.save(jobCandidateExpected);

        assertEquals(response.getId(), jobCandidateReturned.getId());
        assertEquals(response.getName(), jobCandidateReturned.getName());
    }

    @Test
    void when_update_returnSuccess() {
        JobCandidateDto testJobCandidateDto = getJobCandidateDto();
        JobCandidate jobCandidateSaved = getJobCandidate();

        when(jobCandidateRepository.findById(any(Long.class))).thenReturn(Optional.of(jobCandidateSaved));
        when(jobCandidateRepository.save(any(JobCandidate.class))).thenReturn(jobCandidateSaved);

        JobCandidate response = jobCandidateService.update(testJobCandidateDto);

        assertEquals(response.getId(), jobCandidateSaved.getId());
        assertEquals(response.getName(), jobCandidateSaved.getName());
        ArgumentCaptor<JobCandidate> argument = ArgumentCaptor.forClass(JobCandidate.class);
        verify(jobCandidateRepository).save(argument.capture());
        assertEquals("Test", argument.getValue().getName());
        verify(jobCandidateRepository, times(1)).save(argument.getValue());

    }

    @Test
    void when_update_entityNotFound_Throws() {
        JobCandidateDto testJobCandidateDto = getJobCandidateDto();

        when(jobCandidateRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            jobCandidateService.update(testJobCandidateDto);
        });
    }

    @Test
    void when_delete_returnSuccess() {
        Long id = 1L;

        jobCandidateRepository.deleteById(id);

        verify(jobCandidateRepository, times(1)).deleteById(id);


    }

    @Test
    void when_search_returnSuccess() {
        List<Long> skillsIds = getSkillIds();
        Integer pageNo = 1;
        Page<JobCandidate> jobCandidates = new PageImpl<>(getJobCandidateList());

        when(jobCandidateRepository.findBySkillsId(skillsIds, skillsIds.size(), PageRequest.of(pageNo, 5))).thenReturn(jobCandidates);

        Page<JobCandidate> response = jobCandidateRepository.findBySkillsId(getSkillIds(), getSkillIds().size(), PageRequest.of(pageNo, 5));

        assertEquals(response.getSize(), jobCandidates.getContent().size());

    }

    @Test
    void when_getAll_returnSuccess() {
        Integer pageNo = 1;
        Page<JobCandidate> jobCandidates = new PageImpl<>(getJobCandidateList());

        when(jobCandidateRepository.findAll(PageRequest.of(pageNo, 5))).thenReturn(jobCandidates);

        Page<JobCandidate> response = jobCandidateRepository.findAll(PageRequest.of(pageNo, 5));

        assertEquals(response.getSize(), jobCandidates.getContent().size());

    }

    @Test
    void when_existsById_returnSuccess() {
        Long id = 1L;

        when(jobCandidateRepository.existsById(id)).thenReturn(true);

        boolean response = jobCandidateRepository.existsById(id);

        assertTrue(response);
    }

}