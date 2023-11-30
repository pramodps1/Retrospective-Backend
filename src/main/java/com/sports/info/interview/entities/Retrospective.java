package com.sports.info.interview.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Retrospective {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackItem> feedbackItems = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String summary;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ElementCollection
    private List<String> participants;

}
