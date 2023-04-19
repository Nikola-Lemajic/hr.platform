package com.hr.platform.controller;

import com.hr.platform.dto.JobCandidateDto;
import com.hr.platform.dto.SkillDto;
import com.hr.platform.model.JobCandidate;
import com.hr.platform.model.Skill;
import com.hr.platform.service.JobCandidateService;
import com.hr.platform.support.JobCandidateDtoToJobCandidate;
import com.hr.platform.support.JobCandidateToJobCandidateDto;
import com.hr.platform.support.SkillDtoToSkill;
import com.hr.platform.support.SkillToSkillDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JobCandidateControllerTest {

    private JobCandidateService jobCandidateService;
    private JobCandidateToJobCandidateDto toJobCandidateDto;
    private JobCandidateDtoToJobCandidate toJobCandidate;
    private JobCandidateController jobCandidateController;

    @BeforeEach
    void setup() {
        jobCandidateService = Mockito.mock(JobCandidateService.class);
        toJobCandidateDto = new JobCandidateToJobCandidateDto(new SkillToSkillDto());
        toJobCandidate = new JobCandidateDtoToJobCandidate(new SkillDtoToSkill());

        jobCandidateController = new JobCandidateController(jobCandidateService, toJobCandidateDto, toJobCandidate);
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

        jobCandidateDto.setName("Test");
        jobCandidateDto.setEmail("test@email.com");
        jobCandidateDto.setContactNumber("069101101");
        jobCandidateDto.setDateOfBirth(LocalDate.of(2000, 4, 12));
        jobCandidateDto.setSkills(skillDtos);

        return jobCandidateDto;
    }

    private JobCandidateDto getJobCandidateDtoPut() {

        JobCandidateDto jobCandidateDto = getJobCandidateDto();
        jobCandidateDto.setId(1L);

        return jobCandidateDto;
    }

    @Test
    void when_getOne_validRequests_returnSuccess() {

        JobCandidate jobCandidate = getJobCandidate();

        when(jobCandidateService.findOne(1L)).thenReturn(Optional.of(jobCandidate));

        ResponseEntity<JobCandidateDto> response = jobCandidateController.getOne(1L);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), jobCandidate.getId());
        assertEquals(response.getBody().getName(), jobCandidate.getName());
    }

    @Test
    void when_getOne_candidateNotFound_returnsNotFound() {

        when(jobCandidateService.findOne(any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<JobCandidateDto> response = jobCandidateController.getOne(1L);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void when_getAllByName_returnSuccess() {

        List<JobCandidate> jobCandidatesList = getJobCandidateList();
        Page<JobCandidate> jobCandidatePage = new PageImpl<>(jobCandidatesList);

        when(jobCandidateService.findAllByName(any(String.class), any(Integer.class))).thenReturn(jobCandidatePage);

        ResponseEntity<List<JobCandidateDto>> response = jobCandidateController.getAll("Test", 0);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), toJobCandidateDto.convert(jobCandidatePage.getContent()).size());
    }

    @Test
    void when_getAllBySkill_returnSuccess() {

        List<JobCandidate> jobCandidatesList = getJobCandidateList();
        Page<JobCandidate> jobCandidatePage = new PageImpl<>(jobCandidatesList);
        List<JobCandidateDto> expectedResponseDto = toJobCandidateDto.convert(jobCandidatePage.getContent());
        List<Long> skillsIds = getSkillIds();

        when(jobCandidateService.search(skillsIds, 0)).thenReturn(jobCandidatePage);

        ResponseEntity<List<JobCandidateDto>> response = jobCandidateController.getAll(skillsIds, 0);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), expectedResponseDto.size());

        for (JobCandidateDto jobCandidateDto : response.getBody()) {
            assertTrue(expectedResponseDto.stream().anyMatch(j -> jobCandidateDto.getId().equals(j.getId())));
        }
    }

    @Test
    void when_getAll_returnSuccess() {

        List<JobCandidate> jobCandidatesList = getJobCandidateList();
        Page<JobCandidate> jobCandidatePage = new PageImpl<>(jobCandidatesList);

        when(jobCandidateService.getAll(0)).thenReturn(jobCandidatePage);

        ResponseEntity<List<JobCandidateDto>> response = jobCandidateController.getAll(0);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), toJobCandidateDto.convert(jobCandidatePage.getContent()).size());
    }

    @Test
    void when_create_returnSuccess() {

        JobCandidate jobCandidateExpected = toJobCandidate.convert(getJobCandidateDto());
        JobCandidate jobCandidateReturned = toJobCandidate.convert(getJobCandidateDto());
        jobCandidateReturned.setId(1L);

        when(jobCandidateService.save(jobCandidateExpected)).thenReturn(jobCandidateReturned);

        ResponseEntity<JobCandidateDto> response = jobCandidateController.create(getJobCandidateDto());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), jobCandidateReturned.getId());

    }

    @Test
    void when_update_returnSuccess() {

        JobCandidate jobCandidateExpected = toJobCandidate.convert(getJobCandidateDtoPut());
        JobCandidate jobCandidateReturned = toJobCandidate.convert(getJobCandidateDtoPut());
        jobCandidateExpected.setId(getJobCandidateDtoPut().getId());
        jobCandidateReturned.setId(getJobCandidateDtoPut().getId());
        JobCandidateDto jobCandidateDto = getJobCandidateDtoPut();
        Long id = 1L;

        when(jobCandidateService.update(jobCandidateDto)).thenReturn(jobCandidateReturned);
        when(!jobCandidateService.existsById(jobCandidateDto.getId()) || !id.equals(jobCandidateDto.getId())).thenReturn(true);

        ResponseEntity<JobCandidateDto> response = jobCandidateController.update(id, jobCandidateDto);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), jobCandidateReturned.getId());
    }

    @Test
    void when_update_returnBadRequest() {

        JobCandidateDto jobCandidateDto = getJobCandidateDto();
        Long id = 3L;

        when(!jobCandidateService.existsById(jobCandidateDto.getId()) || !id.equals(jobCandidateDto.getId())).thenReturn(false);

        ResponseEntity<JobCandidateDto> response = jobCandidateController.update(id, jobCandidateDto);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    @Test
    void when_delete_Success() {

        JobCandidate jobCandidate = toJobCandidate.convert(getJobCandidateDtoPut());
        Long id = jobCandidate.getId();

        when(jobCandidateService.existsById(jobCandidate.getId())).thenReturn(true);

        ResponseEntity<Void> response = jobCandidateController.delete(id);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNull(response.getBody());
    }

}