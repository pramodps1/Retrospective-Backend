package com.sports.info.interview.service;

import com.sports.info.interview.entities.FeedbackItem;
import com.sports.info.interview.entities.Retrospective;
import com.sports.info.interview.repo.RetrospectiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RetrospectiveService {
    private final RetrospectiveRepository retrospectiveRepository;

    public Retrospective createRetrospective(Retrospective retrospective) {
        // Validate required fields
        if (retrospective.getDate() == null || retrospective.getParticipants().isEmpty()) {
            throw new IllegalArgumentException("Date and participants are required.");
        }

        return retrospectiveRepository.save(retrospective);
    }

    public Retrospective addFeedbackItem(Long retrospectiveId, FeedbackItem feedbackItem) {
        Retrospective retrospective = getRetrospectiveById(retrospectiveId);
        feedbackItem.setId(null);
        try{
        retrospective.getFeedbackItems().add(feedbackItem);
        }catch (Exception e){
            System.out.println(e);
        }
        return retrospectiveRepository.save(retrospective);
    }

    public Retrospective updateFeedbackItem(Long retrospectiveId, Long feedbackItemId, FeedbackItem updatedFeedbackItem) {
        Retrospective retrospective = getRetrospectiveById(retrospectiveId);
        FeedbackItem feedbackItem = retrospective.getFeedbackItems().stream()
                .filter(item -> item.getId().equals(feedbackItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Feedback item not found"));

        feedbackItem.setBody(updatedFeedbackItem.getBody());
        feedbackItem.setType(updatedFeedbackItem.getType());

        return retrospectiveRepository.save(retrospective);
    }

    public Page<Retrospective> getAllRetrospectives(int page, int pageSize) {
        return retrospectiveRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Page<Retrospective> searchRetrospectivesByDate(Date date, int page, int pageSize) {
        return retrospectiveRepository.findByDate(date, PageRequest.of(page, pageSize));
    }

    private Retrospective getRetrospectiveById(Long retrospectiveId) {
        return retrospectiveRepository.findById(retrospectiveId)
                .orElseThrow(() -> new IllegalArgumentException("Retrospective not found"));
    }
}

