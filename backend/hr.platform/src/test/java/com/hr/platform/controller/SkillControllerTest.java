package com.hr.platform.controller;

import com.hr.platform.dto.SkillDto;
import com.hr.platform.model.Skill;
import com.hr.platform.service.SkillService;
import com.hr.platform.support.SkillDtoToSkill;
import com.hr.platform.support.SkillToSkillDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SkillControllerTest {

    private SkillService skillService;
    private SkillToSkillDto toSkillDto;
    private SkillDtoToSkill toSkill;
    private SkillController skillController;

    @BeforeEach
    void setup() {
        skillService = Mockito.mock(SkillService.class);
        toSkill = new SkillDtoToSkill();
        toSkillDto = new SkillToSkillDto();
        skillController = new SkillController(skillService, toSkillDto, toSkill);
    }

    private SkillDto getSkillDto() {

        SkillDto skillDto = new SkillDto();
        skillDto.setName("Test Skill");

        return skillDto;
    }

    private Skill getSkill() {

        return new Skill(1L, "TestSkill");
    }


    private List<SkillDto> getSkillListDto() {

        List<SkillDto> skillListDto = new ArrayList<>();
        skillListDto.add(new SkillDto(1L, "TestSkill"));
        skillListDto.add(new SkillDto(2L, "TestSkill2"));

        return skillListDto;

    }

    private static List<Skill> getSkills() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1L, "TestSkill"));
        skills.add(new Skill(2L, "TestSkill2"));

        return skills;
    }

    @Test
    void when_getAll_returnSuccess() {

        List<Skill> skillList = getSkills();

        when(skillService.search()).thenReturn(skillList);

        ResponseEntity<List<SkillDto>> response = skillController.getAll();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), skillList.size());
    }

    @Test
    void when_create_returnSuccess() {

        Skill skillExpected = toSkill.convert(getSkillDto());
        Skill skillReturned = toSkill.convert(getSkillDto());
        skillReturned.setId(1L);
        SkillDto skillDto = getSkillDto();

        when(skillService.save(skillExpected)).thenReturn(skillReturned);

        ResponseEntity<SkillDto> response = skillController.create(skillDto);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), skillReturned.getId());

    }

}