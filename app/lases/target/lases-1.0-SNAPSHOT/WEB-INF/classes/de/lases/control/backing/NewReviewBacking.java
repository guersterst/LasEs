package de.lases.control.backing;

import de.lases.business.service.ReviewService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

@RequestScoped
@Named
public class NewReviewBacking {

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
