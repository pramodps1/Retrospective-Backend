package com.sports.info.interview.service;

import static org.mockito.Mockito.*;

import com.sports.info.interview.entities.FeedbackItem;
import com.sports.info.interview.entities.FeedbackType;
import com.sports.info.interview.entities.Retrospective;
import com.sports.info.interview.exception.RetrospectiveNotFoundException;
import com.sports.info.interview.repo.RetrospectiveRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RetrospectiveServiceTest {

    @Mock
    private RetrospectiveRepository retrospectiveRepository;

    @InjectMocks
    private RetrospectiveService retrospectiveService;

    public RetrospectiveServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

   // @Test
    void testAddFeedbackItem() {
        // Given
        Long retrospectiveId = 1L;
        Retrospective existingRetrospective = createSampleRetrospectiveWithFeedback();
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setPerson("Bob");
        feedbackItem.setBody("Good collaboration");
        feedbackItem.setType(FeedbackType.POSITIVE);

        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(java.util.Optional.of(existingRetrospective));
        when(retrospectiveRepository.save(any(Retrospective.class))).thenReturn(existingRetrospective);

        // When
        Retrospective updatedRetrospective = retrospectiveService.addFeedbackItem(retrospectiveId, feedbackItem);

        // Then
        assertThat(updatedRetrospective.getFeedbackItems()).hasSize(2); // Assuming one feedback item already exists
        assertThat(updatedRetrospective.getFeedbackItems().get(1).getPerson()).isEqualTo("Bob");
        assertThat(updatedRetrospective.getFeedbackItems().get(1).getBody()).isEqualTo("Good collaboration");
        assertThat(updatedRetrospective.getFeedbackItems().get(1).getType()).isEqualTo(FeedbackType.POSITIVE);

        verify(retrospectiveRepository, times(1)).findById(retrospectiveId);
        verify(retrospectiveRepository, times(1)).save(existingRetrospective);
        verifyNoMoreInteractions(retrospectiveRepository);
    }

    //@Test
    void testAddFeedbackItemRetrospectiveNotFound() {
        // Given
        Long retrospectiveId = 1L;
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setPerson("Bob");
        feedbackItem.setBody("Good collaboration");
        feedbackItem.setType(FeedbackType.POSITIVE);

        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(java.util.Optional.empty());

        // When/Then
        assertThatExceptionOfType(RetrospectiveNotFoundException.class)
                .isThrownBy(() -> retrospectiveService.addFeedbackItem(retrospectiveId, feedbackItem));

        verify(retrospectiveRepository, times(1)).findById(retrospectiveId);
        verifyNoMoreInteractions(retrospectiveRepository);
    }

    @Test
    void testSearchRetrospectivesByDate() {
        // Given
        Date searchDate = java.sql.Date.valueOf("2023-01-01");
        int page = 0;
        int pageSize = 10;

        when(retrospectiveRepository.findByDate(searchDate, PageRequest.of(page, pageSize)))
                .thenReturn(createSampleRetrospectives());

        // When
        Page<Retrospective> retrospectives = retrospectiveService.searchRetrospectivesByDate(searchDate, page, pageSize);

        // Then
        assertThat(retrospectives.getContent()).hasSize(2); // Assuming two sample retrospectives
        assertThat(retrospectives.getContent().get(0).getName()).isEqualTo("SprintReview-001");
        assertThat(retrospectives.getContent().get(1).getName()).isEqualTo("SprintReview-002");

        verify(retrospectiveRepository, times(1)).findByDate(searchDate, PageRequest.of(page, pageSize));
        verifyNoMoreInteractions(retrospectiveRepository);
    }

    // Helper method to create a sample Retrospective with feedback for testing
    private Retrospective createSampleRetrospectiveWithFeedback() {
        Retrospective retrospective = new Retrospective();
        retrospective.setId(1L);
        retrospective.setName("SprintReview-001");
        retrospective.setSummary("Our first sprint review");
        retrospective.setDate(new Date());
        retrospective.setParticipants(Arrays.asList("John Doe", "Jane Smith"));

        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setPerson("Alice");
        feedbackItem.setBody("Great teamwork!");
        feedbackItem.setType(FeedbackType.PRAISE);

        retrospective.setFeedbackItems(Arrays.asList(feedbackItem));

        return retrospective;
    }

    // Helper method to create sample retrospectives for testing search
    private Page<Retrospective> createSampleRetrospectives() {
        Retrospective retrospective1 = new Retrospective();
        retrospective1.setId(1L);
        retrospective1.setName("SprintReview-001");
        retrospective1.setSummary("Our first sprint review");
        retrospective1.setDate(new Date());
        retrospective1.setParticipants(Arrays.asList("John Doe", "Jane Smith"));

        Retrospective retrospective2 = new Retrospective();
        retrospective2.setId(2L);
        retrospective2.setName("SprintReview-002");
        retrospective2.setSummary("Another sprint review");
        retrospective2.setDate(new Date());
        retrospective2.setParticipants(Arrays.asList("Bob", "Alice"));

        List<Retrospective> retrospectives = Arrays.asList(retrospective1, retrospective2);

        return new PageImpl<>(retrospectives);
    }
}
