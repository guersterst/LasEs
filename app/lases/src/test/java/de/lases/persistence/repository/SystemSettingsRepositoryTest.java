package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sebastian Vogt
 */
@ExtendWith(WeldJunit5Extension.class)
class SystemSettingsRepositoryTest {

    private static SystemSettings systemSettings;

    private static Transaction transaction;

    @BeforeAll
    static void initSystemSettings() {
        systemSettings = new SystemSettings();
        systemSettings.setImprint("imprint");
        systemSettings.setCompanyName("company");
        systemSettings.setHeadlineWelcomePage("welcome");
        systemSettings.setMessageWelcomePage("hello world");
        systemSettings.setStyle("dark.css");
    }

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class clazz = TransactionTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
        transaction = new Transaction();
    }

    @AfterEach
    void shutDownConnectionPool() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

    static boolean allFieldsTheSame(SystemSettings systemSettings1,
                                    SystemSettings systemSettings2) {
        return
                systemSettings1.getImprint()
                .equals(systemSettings2.getImprint())
                && systemSettings1.getCompanyName()
                .equals(systemSettings2.getCompanyName())
                && systemSettings1.getHeadlineWelcomePage()
                .equals(systemSettings2.getHeadlineWelcomePage())
                && systemSettings1.getMessageWelcomePage()
                .equals(systemSettings2.getMessageWelcomePage())
                && systemSettings1.getStyle()
                .equals(systemSettings2.getStyle());
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testUpdateAndGet() throws DataNotWrittenException {
        SystemSettingsRepository.updateSettings(systemSettings, transaction);
        SystemSettings savedSettings = SystemSettingsRepository
                .getSettings(transaction);
        assertTrue(allFieldsTheSame(systemSettings, savedSettings));
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testUpdateWithNull() {
        String oldImprint = systemSettings.getImprint();
        systemSettings.setImprint(null);
        assertThrows(InvalidFieldsException.class, () -> {
            SystemSettingsRepository.updateSettings(systemSettings,
                        transaction);
        });
        systemSettings.setImprint(oldImprint);
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testGetAndSetLogo() throws DataNotWrittenException, NotFoundException {
        FileDTO file = new FileDTO();
        file.setFile(new byte[]{});
        SystemSettingsRepository.setLogo(file, transaction);
        FileDTO returnedFile = SystemSettingsRepository.getLogo(transaction);
        assertAll(
                () -> {
                    assertNotNull(returnedFile);
                },
                () -> {
                    assertEquals(0, returnedFile.getFile().length);
                }
        );
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testSetLogoWithNull() {
        FileDTO file = new FileDTO();
        assertThrows(InvalidFieldsException.class, () ->
                SystemSettingsRepository.setLogo(file, transaction));
    }

}