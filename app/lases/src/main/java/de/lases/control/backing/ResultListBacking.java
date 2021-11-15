package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;

@ViewScoped
@Named
public class ResultListBacking implements Serializable {

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private Pagination<User> userPagination;

    private Pagination<ScientificForum> scientificForumPagination;

    private SubmissionService submissionListService;
    private ScientificForumService scientificForumService;
    private UserService userService;

    private SessionInformation sessionInformation;

    private SubmissionState stateFilterSelectSub;

    private SubmissionState stateFilterSelectEdit;

    private SubmissionState stateFilterSelectReview;

    private DateSelect submission;

    private DateSelect editorial;

    private DateSelect reviewing;

    private DateSelect forum;

    private LocalDate dateDeadlineLowerFilterSciForum;

    private LocalDate dateDeadlineUpperFilterSciFOrum;

    @PostConstruct
    public void init() {
    }

}
