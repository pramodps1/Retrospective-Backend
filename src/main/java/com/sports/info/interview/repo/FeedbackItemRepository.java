package com.sports.info.interview.repo;

import com.sports.info.interview.entities.FeedbackItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackItemRepository extends JpaRepository<FeedbackItem, Long> {
    // Add custom queries if needed
}
