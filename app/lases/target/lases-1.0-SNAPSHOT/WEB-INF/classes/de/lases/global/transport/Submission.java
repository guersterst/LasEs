package de.lases.global.transport;

import java.time.LocalDateTime;
import java.util.List;

public class Submission {

    private List<Paper> papers;

    private ScientificForum scientificForum;

    private int id;

    private boolean revisionRequired;

    private List<User> reviewers;

    private User author;

    private List<User> coAuthors;

    private User editor;


    private String title;

    private SubmissionState state;

    private LocalDateTime deadlineRevision;

    private LocalDateTime submissionTime;

}
