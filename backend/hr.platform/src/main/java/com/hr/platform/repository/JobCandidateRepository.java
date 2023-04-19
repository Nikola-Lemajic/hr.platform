package com.hr.platform.repository;

import com.hr.platform.model.JobCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCandidateRepository extends JpaRepository<JobCandidate,Long> {

    Page<JobCandidate> findByNameContainsIgnoreCase(String name, PageRequest pageNo);

    @Query(value = "SELECT jk " +
            "FROM job_candidate jk " +
            "LEFT JOIN job_candidate_skill jcs " +
            "ON jk.id = jcs.jobCandidateId " +
            "WHERE jcs.skillId IN (:skillsIds) " +
            "GROUP BY jk.id HAVING COUNT(jk.id) = :skillsIdsSize ")
    Page<JobCandidate> findBySkillsId(@Param("skillsIds") List<Long> skillsIds,@Param("skillsIdsSize") int skillsIdsSize,PageRequest pageNo);


}
