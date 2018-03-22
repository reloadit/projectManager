package com.copyrightException.ProjectManager;

import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.exceptions.NotLoggedInException;
import com.vaadin.ui.UI;

public class Helper {

    private static final String CURRENT_USER_PROPERTY = "currentUser";

    public static User getUser() {
        final Object obj = UI.getCurrent().getSession().getAttribute(CURRENT_USER_PROPERTY);
        if (obj instanceof User) {
            return (User) obj;
        } else {
            throw new NotLoggedInException();
        }
    }

    public static void setUser(final User user) {
        UI.getCurrent().getSession().setAttribute(CURRENT_USER_PROPERTY, user);
    }
}
