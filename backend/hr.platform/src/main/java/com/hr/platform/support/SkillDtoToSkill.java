package com.hr.platform.support;

import com.hr.platform.dto.SkillDto;
import com.hr.platform.model.Skill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SkillDtoToSkill implements Converter<SkillDto, Skill> {

    @Override
    public Skill convert(SkillDto skillDto) {

        Skill skill = new Skill();

        if (skillDto.getId() != null) {
            skill.setId(skillDto.getId());
        }
        skill.setName(skillDto.getName());

        return skill;
    }

    public List<Skill> convert(List<SkillDto> skills) {

        return skills.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
