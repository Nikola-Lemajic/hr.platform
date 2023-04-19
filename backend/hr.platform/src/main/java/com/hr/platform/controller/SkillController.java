package com.hr.platform.controller;

import com.hr.platform.dto.SkillDto;
import com.hr.platform.model.Skill;
import com.hr.platform.service.SkillService;
import com.hr.platform.support.SkillDtoToSkill;
import com.hr.platform.support.SkillToSkillDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/skill", produces = MediaType.APPLICATION_JSON_VALUE)
public class SkillController {

    private final SkillService skillService;

    private final SkillToSkillDto toSkillDto;

    private final SkillDtoToSkill toSkill;

    @Autowired
    public SkillController(SkillService skillService, SkillToSkillDto toSkillDto, SkillDtoToSkill toSkill) {
        this.skillService = skillService;
        this.toSkillDto = toSkillDto;
        this.toSkill = toSkill;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SkillDto>> getAll() {

        List<Skill> skills = skillService.search();

        return new ResponseEntity<>(toSkillDto.convert(skills), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillDto> create(@RequestBody SkillDto skillDto) {

        if (skillDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Skill skill = toSkill.convert(skillDto);
        Skill savedSkill = skillService.save(skill);

        return new ResponseEntity<>(toSkillDto.convert(savedSkill), HttpStatus.OK);
    }
}
