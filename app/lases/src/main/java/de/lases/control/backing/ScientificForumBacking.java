package de.lases.control.backing;

import de.lases.business.service.ScienceFieldService;
import de.lases.business.service.ScientificForumService;
import de.lases.business.service.SubmissionService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.util.List;

@ViewScoped
@Named
public class ScientificForumBacking {

    private String newEditorInput;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewerPagination;

    private Pagination<Submission> editedPagination;

    private SessionInformation sessionInformation;

    private SubmissionService submissionListService;

    private ScientificForumService scientificForumService;

    private ScientificForum scientificForum;

    private ScienceFieldService scienceFieldService;

    private List<Submission> submissions;

    private List<User> editors;

    private List<ScienceField> scieneFields;

    private ScienceField selectedScienceFieldInput;

    private SubmissionState stateFilterSelectSub;

    private SubmissionState stateFilterSelectEdit;

    private SubmissionState stateFilterSelectReview;

    private DateSelect submission;

    private DateSelect reviewing;

    private DateSelect editorial;

    @PostConstruct
    public void init() {
    }

    public ScientificForum getScientificForum() {
        return null;
    }

    public void updateScientificForum(ScientificForum scientificForum) {
    }

    // Hier resultlistparams übergeben.
    public void getSubmissions() {
    }

    public void deleteForum() {
    }

    public void addEditor() {
    }

    public void addScienceField() {
    }

    public void removeEditor() {
    }

    public void removeScienceField() {
    }

    public void submitChanges() {
    }

}
