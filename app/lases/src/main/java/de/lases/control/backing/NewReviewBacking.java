package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.Serializable;

@RequestScoped
@Named
public class NewReviewBacking implements Serializable {

    private ReviewService reviewService;

    private SessionInformation sessionInformation;

    private Part uploadedPDF;

    private Review review;

    @PostConstruct
    public void init() {
    }

    public String addReview() {
        return null;
    }

}
