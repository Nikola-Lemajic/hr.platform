package com.hr.platform.support;

import com.hr.platform.dto.SkillDto;
import com.hr.platform.model.Skill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SkillToSkillDto implements Converter<Skill, SkillDto> {

    @Override
    public SkillDto convert(Skill skill) {

        SkillDto skillDto = new SkillDto();

        skillDto.setId(skill.getId());
        skillDto.setName(skill.getName());

        return skillDto;
    }

    public List<SkillDto> convert(List<Skill> skills) {

        return skills.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
