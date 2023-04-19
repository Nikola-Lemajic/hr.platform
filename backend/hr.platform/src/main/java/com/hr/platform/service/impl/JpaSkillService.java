package com.hr.platform.service.impl;

import com.hr.platform.model.Skill;
import com.hr.platform.repository.SkillRepository;
import com.hr.platform.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaSkillService implements SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public JpaSkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public List<Skill> search() {
        return skillRepository.findAll();
    }
}
