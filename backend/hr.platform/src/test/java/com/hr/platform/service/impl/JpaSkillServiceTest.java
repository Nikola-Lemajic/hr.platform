package com.hr.platform.service.impl;

import com.hr.platform.model.Skill;
import com.hr.platform.repository.SkillRepository;
import com.hr.platform.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JpaSkillServiceTest {

    private SkillRepository skillRepository;
    private SkillService skillService;

    @BeforeEach
    void setup() {
        skillRepository = Mockito.mock(SkillRepository.class);
        skillService = new JpaSkillService(skillRepository);
    }

    private Skill getSkill() {

        return new Skill(1L, "TestSkill");
    }

    private static List<Skill> getSkills() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1L, "TestSkill"));
        skills.add(new Skill(2L, "TestSkill2"));

        return skills;
    }

    @Test
    void when_save_returnSuccess() {
        Skill skill = getSkill();
        Skill savedSkill = getSkill();

        when(skillRepository.save(skill)).thenReturn(savedSkill);

        Skill response = skillService.save(skill);

        assertEquals(response.getId(), savedSkill.getId());
        assertEquals(response.getName(), savedSkill.getName());
    }

    @Test
    void when_findAll_returnSuccess() {
        List<Skill> skillList = getSkills();

        when(skillRepository.findAll()).thenReturn(skillList);

        List<Skill> response = skillService.search();

        assertEquals(response.size(), skillList.size());
    }

}