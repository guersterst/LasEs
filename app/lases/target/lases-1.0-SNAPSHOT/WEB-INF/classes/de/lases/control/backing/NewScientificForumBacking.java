package de.lases.control.backing;

import java.io.Serializable;
import java.util.List;

import de.lases.business.service.*;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
public class NewScientificForumBacking implements Serializable {

    private ScienceFieldService scienceFieldService;

    private ScientificForumService scientificForumService;

    private ScientificForum newScientificForum;

    private SessionInformation sessionInformation;

    private String editorEmailInput;

    private List<User> editors;

    private List<ScienceField> scienceFields;

    private List<ScienceField> selectedScienceFields;

    private ScienceField scienceFieldSelectionInput;

    private String newScienceFieldInput;


    @PostConstruct
    public void init() {
    }

    public void addEditor() {
    }

    public void removeEditor(int id) {
    }

    public void addNewScienceField() {
    }

    public void addScienceField() {
    }

    public void create() {
    }

    public void abort() {
    }

}
