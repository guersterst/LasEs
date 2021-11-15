package de.lases.control.backing;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
public class ImprintBacking {

    private String imprint;

    @PostConstruct
    public void init() {
    }

}
