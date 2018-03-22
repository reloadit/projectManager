package com.copyrightException.ProjectManager;

import com.copyrightException.ProjectManager.exceptions.NotLoggedInException;
import com.copyrightException.ProjectManager.views.login.LoginView;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcceptionHandler extends DefaultErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExcceptionHandler.class);

    @Override
    public void error(ErrorEvent event) {
        for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {
            if (t instanceof NotLoggedInException) {
                LOG.error("User not logged in");
                final UI ui = UI.getCurrent();
                ui.getNavigator().navigateTo(LoginView.VIEW_NAME);
                ui.getWindows().forEach(window -> ui.removeWindow(window));
                return;
            }
        }
        doDefault(event);
    }

}
