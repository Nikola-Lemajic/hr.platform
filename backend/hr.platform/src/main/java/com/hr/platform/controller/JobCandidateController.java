package com.hr.platform.controller;

import com.hr.platform.dto.JobCandidateDto;
import com.hr.platform.model.JobCandidate;
import com.hr.platform.service.JobCandidateService;
import com.hr.platform.support.JobCandidateDtoToJobCandidate;
import com.hr.platform.support.JobCandidateToJobCandidateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api/jobCandidate", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobCandidateController {

    private final JobCandidateService jobCandidateService;

    private final JobCandidateToJobCandidateDto toJobCandidateDto;

    private final JobCandidateDtoToJobCandidate toJobCandidate;

    @Autowired
    public JobCandidateController(
            JobCandidateService jobCandidateService,
            JobCandidateToJobCandidateDto toJobCandidateDto,
            JobCandidateDtoToJobCandidate toJobCandidate
    ) {
        this.jobCandidateService = jobCandidateService;
        this.toJobCandidateDto = toJobCandidateDto;
        this.toJobCandidate = toJobCandidate;
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<JobCandidateDto> getOne(@PathVariable Long id) {

        return jobCandidateService.findOne(id)
                .map(jobCandidate -> new ResponseEntity<>(toJobCandidateDto.convert(jobCandidate), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getAllByName/{name}")
    public ResponseEntity<List<JobCandidateDto>> getAll(
            @PathVariable String name,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {

        Page<JobCandidate> jobCandidates = jobCandidateService.findAllByName(name, pageNo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Total-Pages", Integer.toString(jobCandidates.getTotalPages()));

        return new ResponseEntity<>(toJobCandidateDto.convert(jobCandidates.getContent()), headers, HttpStatus.OK);
    }

    @GetMapping("/getAllBySkill")
    public ResponseEntity<List<JobCandidateDto>> getAll(
            @RequestParam List<Long> skillsIds,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {

        if (skillsIds == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Page<JobCandidate> jobCandidates = jobCandidateService.search(skillsIds, pageNo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Total-Pages", Integer.toString(jobCandidates.getTotalPages()));

        return new ResponseEntity<>(toJobCandidateDto.convert(jobCandidates.getContent()), headers, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<JobCandidateDto>> getAll(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo) {

        Page<JobCandidate> jobCandidates = jobCandidateService.getAll(pageNo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Total-Pages", Integer.toString(jobCandidates.getTotalPages()));

        return new ResponseEntity<>(toJobCandidateDto.convert(jobCandidates.getContent()), headers, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobCandidateDto> create(@RequestBody JobCandidateDto jobCandidateDto) {

        if (jobCandidateDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        JobCandidate jobCandidate = toJobCandidate.convert(jobCandidateDto);
        JobCandidate savedJobCandidate = jobCandidateService.save(jobCandidate);

        return new ResponseEntity<>(toJobCandidateDto.convert(savedJobCandidate), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobCandidateDto> update(@PathVariable Long id, @RequestBody JobCandidateDto jobCandidateDto) {

        if (!id.equals(jobCandidateDto.getId()) || !jobCandidateService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            JobCandidate jobCandidate = jobCandidateService.update(jobCandidateDto);
            return new ResponseEntity<>(toJobCandidateDto.convert(jobCandidate), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!jobCandidateService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        jobCandidateService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
