package com.hr.platform.model;

import jakarta.persistence.*;

@Entity(name = "job_candidate_skill")
public class JobCandidateSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_candidate_id")
    private Long jobCandidateId;

    @Column(name = "skill_id")
    private Long skillId;
}
