package de.lases.control.internal;

import de.lases.business.internal.ConfigPropagator;
import de.lases.business.internal.Lifetime;
import de.lases.global.transport.FileDTO;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Listens for startup and shutdown of the system and executes initialization
 * and cleanup tasks respectively.
 *
 * @author Thomas Kirz
 */
@WebListener
public class LifetimeListener implements ServletContextListener {

    @Inject
    private ConfigPropagator configPropagator;

    private final static String APPLICATION_CONFIG_PATH =
            "/WEB-INF/config/config.properties";
    private final static String LOGGER_CONFIG_PATH =
            "/WEB-INF/config/logger.properties";

    /**
     * On shutdown we make sure all used resources are closed gracefully.
     * <p>
     * We perform all already started operations on the following
     * resources and then make sure to free them properly:
     * <ul>
     *     <li> The datasource </li>
     *     <li> The messaging service </li>
     *     <li> The logger </li>
     * </ul>
     *
     * @param event The shutdown event.
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // First answer + top comment
        // https://stackoverflow.com/questions/1549924/shutdown-hook-for-java-web-application
        // Top comment:
        // https://stackoverflow.com/questions/9173132/stop-scheduled-timer-when-shutdown-tomcat/9186070#9186070
        Lifetime.shutdown();
        Logger.getLogger(LifetimeListener.class.getName()).info("DB Pool Shutdown Hook executed.");
    }

    /**
     * On startup we initialize the resources used by the system.
     * <p>
     * Specifically we read the config file and start:
     * <ul>
     *     <li> The datasource, specifically the connection pool </li>
     *     <li> The messaging service </li>
     *     <li> The logger </li>
     * </ul>
     *
     * @param sce The startup event.
     */
    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext sctx = sce.getServletContext();
        initializeAppConfig(sctx);
        FileDTO loggerConfigFile = new FileDTO();
        loggerConfigFile.setInputStream(sctx.getResourceAsStream(LOGGER_CONFIG_PATH));
        Lifetime.startup(loggerConfigFile);
    }

    private void initializeAppConfig(ServletContext sctx) {
        InputStream is = sctx.getResourceAsStream(APPLICATION_CONFIG_PATH);
        FileDTO configFile = new FileDTO();
        configFile.setInputStream(is);
        configPropagator.setProperties(configFile);
    }
}
