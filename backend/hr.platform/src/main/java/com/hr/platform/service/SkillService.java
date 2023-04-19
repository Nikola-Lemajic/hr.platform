package com.hr.platform.service;

import com.hr.platform.model.Skill;

import java.util.List;

public interface SkillService {

    List<Skill> search();

    Skill save(Skill skill);
}
