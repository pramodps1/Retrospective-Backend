package com.sports.info.interview.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FeedbackItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String person;

    private String body;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;
}
