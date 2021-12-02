package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemSettingsRepositoryTest {

    private static SystemSettings systemSettings;

    private static Transaction transaction;

    @BeforeAll
    static void initSystemSettings() {
        transaction = new Transaction();
        systemSettings = new SystemSettings();
        systemSettings.setImprint("imprint");
        systemSettings.setCompanyName("company");
        systemSettings.setHeadlineWelcomePage("welcome");
        systemSettings.setMessageWelcomePage("hello world");
        systemSettings.setStyle("dark.css");
    }

    static boolean allFieldsTheSame(SystemSettings systemSettings1,
                                    SystemSettings systemSettings2) {
        return systemSettings1.getImprint().equals(systemSettings2.getImprint())
                && systemSettings1.getCompanyName()
                .equals(systemSettings2.getCompanyName())
                && systemSettings1.getHeadlineWelcomePage()
                .equals(systemSettings2.getHeadlineWelcomePage())
                && systemSettings1.getMessageWelcomePage()
                .equals(systemSettings2.getMessageWelcomePage())
                && systemSettings1.getStyle()
                .equals(systemSettings2.getStyle());
    }

    @Test
    void updateAndGet() throws DataNotWrittenException {
        SystemSettingsRepository.updateSettings(systemSettings, transaction);
        SystemSettings savedSettings = SystemSettingsRepository
                .getSettings(transaction);
        assertTrue(allFieldsTheSame(systemSettings, savedSettings));
    }

    @Test
    void updateWithNull() {
        String oldImprint = systemSettings.getImprint();
        systemSettings.setImprint(null);
        assertThrows(InvalidFieldsException.class, () -> {
            SystemSettingsRepository.updateSettings(systemSettings,
                        transaction);
        });
        systemSettings.setImprint(oldImprint);
    }

    @Test
    void getAndSetLogo() throws DataNotWrittenException {
        FileDTO file = new FileDTO();
        file.setFile(new byte[]{});
        SystemSettingsRepository.setLogo(file, transaction);
        FileDTO returnedFile = SystemSettingsRepository.getLogo(transaction);
        assertNotNull(returnedFile);
        assertEquals(0, returnedFile.getFile().length);
    }

    @Test
    void setLogoWithNull() {
        FileDTO file = new FileDTO();
        assertThrows(InvalidFieldsException.class, () ->
                SystemSettingsRepository.setLogo(file, transaction));
    }

    @AfterAll
    static void rollbackChanges() {
        transaction.abort();
    }

}