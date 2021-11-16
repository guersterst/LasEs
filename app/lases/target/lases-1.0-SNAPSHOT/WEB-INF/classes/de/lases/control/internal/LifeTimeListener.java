package de.lases.control.internal;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class LifeTimeListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Automatisch erstellter Methoden-Stub

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Automatisch erstellter Methoden-Stub

    }
}
