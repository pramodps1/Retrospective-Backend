package com.sports.info.interview.controller;

import com.sports.info.interview.entities.FeedbackItem;
import com.sports.info.interview.entities.Retrospective;
import com.sports.info.interview.service.RetrospectiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {
    private static final Logger logger = LoggerFactory.getLogger(RetrospectiveController.class);

    private final RetrospectiveService retrospectiveService;

    @Autowired
    public RetrospectiveController(RetrospectiveService retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }

    @PostMapping
    public ResponseEntity<Retrospective> createRetrospective(@RequestBody Retrospective retrospective) {
        logger.info("Received request to create a retrospective: {}", retrospective);
        Retrospective createdRetrospective = retrospectiveService.createRetrospective(retrospective);
        return new ResponseEntity<>(createdRetrospective, HttpStatus.CREATED);
    }


    @PostMapping("/{retrospectiveId}/feedback")
    public ResponseEntity<Retrospective> addFeedbackItem(
            @PathVariable Long retrospectiveId,
            @RequestBody FeedbackItem feedbackItem
    ) {
        logger.info("Received request to add feedback item to retrospective with ID {}: {}", retrospectiveId, feedbackItem);

        Retrospective updatedRetrospective = retrospectiveService.addFeedbackItem(retrospectiveId, feedbackItem);
        return new ResponseEntity<>(updatedRetrospective, HttpStatus.OK);
    }


    @PutMapping("/{retrospectiveId}/feedback/{feedbackItemId}")
    public ResponseEntity<Retrospective> updateFeedbackItem(
            @PathVariable Long retrospectiveId,
            @PathVariable Long feedbackItemId,
            @RequestBody FeedbackItem updatedFeedbackItem
    ) {
        logger.info("Received request to update feedback item (ID {}) in retrospective with ID {}: {}",
                feedbackItemId, retrospectiveId, updatedFeedbackItem);

        Retrospective updatedRetrospective = retrospectiveService.updateFeedbackItem(
                retrospectiveId,
                feedbackItemId,
                updatedFeedbackItem
        );
        return new ResponseEntity<>(updatedRetrospective, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Retrospective>> getAllRetrospectives(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        Page<Retrospective> retrospectives = retrospectiveService.getAllRetrospectives(page, pageSize);
        return new ResponseEntity<>(retrospectives, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Retrospective>> searchRetrospectivesByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        logger.info("Received request to search retrospectives. Date: {}, Page: {}, PageSize: {}", date, page, pageSize);

        Page<Retrospective> retrospectives = retrospectiveService.searchRetrospectivesByDate(date, page, pageSize);
        return new ResponseEntity<>(retrospectives, HttpStatus.OK);
    }
}

