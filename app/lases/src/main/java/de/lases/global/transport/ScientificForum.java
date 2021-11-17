package de.lases.global.transport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a scientific forum.
 */
public class ScientificForum {

    private List<ScienceField> scienceFields;

    private List<Submission> submissions;

    private List<User> editors;

    private int id;


    private String description;

    private String reviewManual;

    private String url;

    private LocalDateTime deadline;

    private String name;

}
