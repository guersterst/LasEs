package de.lases.control.backing;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
public class ErrorPageBacking {

    private String errorMessage;

    private String stackTrace;

    @PostConstruct
    public void init() {
    }


}
