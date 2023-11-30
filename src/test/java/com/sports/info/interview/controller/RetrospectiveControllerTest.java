package com.sports.info.interview.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.info.interview.DateMatcher;
import com.sports.info.interview.entities.FeedbackItem;
import com.sports.info.interview.entities.FeedbackType;
import com.sports.info.interview.entities.Retrospective;
import com.sports.info.interview.service.RetrospectiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RetrospectiveControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RetrospectiveService retrospectiveService;

    @InjectMocks
    private RetrospectiveController retrospectiveController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(retrospectiveController).build();
    }

    @Test
    void testAddFeedbackItem() throws Exception {
        // Assuming you have a valid Retrospective and FeedbackItem
        Long retrospectiveId = 1L;
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setPerson("Alice");
        feedbackItem.setBody("Great teamwork!");
        feedbackItem.setType(FeedbackType.PRAISE);

        when(retrospectiveService.addFeedbackItem(eq(retrospectiveId), any(FeedbackItem.class)))
                .thenReturn(createSampleRetrospectiveWithFeedback());

        mockMvc.perform(post("/retrospectives/{retrospectiveId}/feedback", retrospectiveId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(feedbackItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackItems[0].person").value("Alice"))
                .andExpect(jsonPath("$.feedbackItems[0].body").value("Great teamwork!"))
                .andExpect(jsonPath("$.feedbackItems[0].type").value("PRAISE"));

        verify(retrospectiveService, times(1)).addFeedbackItem(eq(retrospectiveId), any(FeedbackItem.class));
        verifyNoMoreInteractions(retrospectiveService);
    }

   // @Test
    void testSearchRetrospectivesByDate() throws Exception {
        String date="2023-01-01";
        Date searchDate=new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Date a=new Date();
        LocalDate localDate = LocalDate.of(2023, 1, 1);
        Date searchDate2 = java.sql.Date.valueOf(localDate);
        int page = 0;
        int pageSize = 10;

        when(retrospectiveService.searchRetrospectivesByDate(any(Date.class), eq(page), eq(pageSize)))
                .thenReturn(createSampleRetrospectives());

        mockMvc.perform(get("/retrospectives/search")
                        .param("date", "2023-01-01")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("SprintReview-001"))
                .andExpect(jsonPath("$.content[1].name").value("SprintReview-002"));

        verify(retrospectiveService, times(1))
                .searchRetrospectivesByDate(eq(searchDate), eq(page), eq(pageSize));
        verifyNoMoreInteractions(retrospectiveService);
    }

    // Helper method to convert objects to JSON string
    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to create a sample Retrospective with feedback
    private Retrospective createSampleRetrospectiveWithFeedback() {
        Retrospective retrospective = new Retrospective();
        retrospective.setId(1L);
        retrospective.setName("SprintReview-001");
        retrospective.setSummary("Our first sprint review");
        retrospective.setDate(new Date());
        retrospective.setParticipants(Collections.singletonList("John Doe"));

        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setPerson("Alice");
        feedbackItem.setBody("Great teamwork!");
        feedbackItem.setType(FeedbackType.PRAISE);

        retrospective.setFeedbackItems(Collections.singletonList(feedbackItem));

        return retrospective;
    }

    // Helper method to create sample retrospectives for testing search
    private Page<Retrospective> createSampleRetrospectives() {
        Retrospective retrospective1 = new Retrospective();
        retrospective1.setId(1L);
        retrospective1.setName("SprintReview-001");
        retrospective1.setSummary("Our first sprint review");
        retrospective1.setDate(new Date());
        retrospective1.setParticipants(Collections.singletonList("John Doe"));

        Retrospective retrospective2 = new Retrospective();
        retrospective2.setId(2L);
        retrospective2.setName("SprintReview-002");
        retrospective2.setSummary("Another sprint review");
        retrospective2.setDate(new Date());
        retrospective2.setParticipants(Collections.singletonList("Jane Smith"));

        List<Retrospective> retrospectives = List.of(retrospective1, retrospective2);

        return new PageImpl<>(retrospectives);
    }
}

