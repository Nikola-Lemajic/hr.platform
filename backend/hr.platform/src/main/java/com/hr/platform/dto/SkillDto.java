package com.hr.platform.dto;


import jakarta.validation.constraints.NotNull;

public class SkillDto {

    private Long id;

    @NotNull
    private String name;

    public SkillDto() {
    }

    public SkillDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
