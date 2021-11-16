package de.lases.control.backing;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped
@Named
public class ErrorPageBacking implements Serializable {

    private String errorMessage;

    private String stackTrace;

    @PostConstruct
    public void init() {
    }


}
