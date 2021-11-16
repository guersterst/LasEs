package de.lases.global.transport;

import java.time.LocalDate;
import java.util.List;

public class User {

    private Verification verification;

    private List<Review> reviews;

    private List<ScienceField> scienceFields;

    private List<ScientificForum> edits;

    private List<Submission> edited;

    private List<Submission> coAuthored;

    private List<Submission> authored;

    private List<Submission> reviewed;

    private int id;


    private List<Privilege> priveleges;

    private String title;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private LocalDate dateOfBirth;

    private String employer;

    private String passwordHashed;

    private String passwordSalt;

    private boolean isNotRegistered;

    private boolean isEditor() { return false; }

    private boolean isReviewer() { return false; }

    private boolean isAdmin() { return false; }

}
