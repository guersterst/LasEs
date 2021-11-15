package de.lases.control.backing;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped
@Named
public class ImprintBacking implements Serializable {

    private String imprint;

    @PostConstruct
    public void init() {
    }

}
