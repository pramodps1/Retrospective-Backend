package com.sports.info.interview.repo;

import com.sports.info.interview.entities.Retrospective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long> {
    Page<Retrospective> findByDate(Date date, PageRequest of);

}
