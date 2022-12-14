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

    @Test
    void testUpdateAndGet() throws DataNotWrittenException {
        SystemSettingsRepository.updateSettings(systemSettings, transaction);
        SystemSettings savedSettings = SystemSettingsRepository
                .getSettings(transaction);
        assertTrue(allFieldsTheSame(systemSettings, savedSettings));
    }

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

    @Test
    void testGetAndSetLogo() throws DataNotWrittenException {
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

    @Test
    void testSetLogoWithNull() {
        FileDTO file = new FileDTO();
        assertThrows(InvalidFieldsException.class, () ->
                SystemSettingsRepository.setLogo(file, transaction));
    }

    @AfterAll
    static void rollbackChanges() {
        transaction.abort();
    }

}