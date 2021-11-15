package de.lases.control.backing;

import de.lases.business.service.ScientificForumService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named
public class ScientificForumListBacking {

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
