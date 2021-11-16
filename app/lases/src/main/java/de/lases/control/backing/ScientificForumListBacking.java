package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@ViewScoped
@Named
public class ScientificForumListBacking implements Serializable {

    private SessionInformation sessionInformation;

    private Pagination<ScientificForum> scientificForumPagination;

    private ScientificForumService scientificService;

    private DateSelect deadline;

    private String searchString;

    @PostConstruct
    public void init() {
    }

    public void applyFilters() {
    }


}
