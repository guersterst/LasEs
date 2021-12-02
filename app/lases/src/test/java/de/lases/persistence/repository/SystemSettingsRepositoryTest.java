package de.lases.persistence.repository;

import de.lases.global.transport.SystemSettings;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class SystemSettingsRepositoryTest {

    private SystemSettings settings;

    @BeforeAll
    void initSettings() {
        settings.setImprint("imprint");
        settings.setCompanyName("company");
        settings.setHeadlineWelcomePage("welcome");
        settings.setMessageWelcomePage("hello world");

    }

}