package de.lases.global.transport;

import java.time.LocalDateTime;

public class Review {

    /*
     * PDF muast soiba lod'n.
     */

    // TODO @Sebastian: Das hier alles zu einer id aendern...
    // TODO @Sebastian: Submission hier benoetigt? ... ich muss auf alle fälle
    // wissen, zu welcher submission ich zurueck muss bei der newSubmission
    // Seite
    private Paper paper;

    private Submission submission;

    private User reviewer;

    private int id;


    private LocalDateTime timestampUpdloaded;

    private boolean visible;

    private boolean acceptPaper;

    private String comment;


}
