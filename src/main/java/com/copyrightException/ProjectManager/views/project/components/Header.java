package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.views.login.LoginView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Header extends Panel {

    private Button bLogout;
    private Label lName, lDate;

    public Header() {
        super();
        createComponents();
        initLayout();
        initUi();
    }

    private void createComponents() {
        User ownUser = Helper.getUser();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = LocalDate.now();

        lName = new Label("Hello, " + ownUser.getName() + "!");
        lDate = new Label(dtf.format(currentDate));
        bLogout = new Button("Logout");

        bLogout.addStyleName(ValoTheme.BUTTON_PRIMARY);

        bLogout.setIcon(VaadinIcons.EXIT);
    }

    private void initLayout() {
        final GridLayout layout = new GridLayout(4, 1);
        layout.setSpacing(true);
        layout.addComponent(lName, 0, 0);
        layout.addComponent(lDate, 1, 0);
        layout.addComponent(bLogout, 3, 0);
        layout.setComponentAlignment(lName, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(lDate, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(bLogout, Alignment.MIDDLE_CENTER);

        setContent(layout);
        layout.setSizeFull();
    }

    private void initUi() {
        bLogout.addClickListener(e -> onLogout());
    }

    private void onLogout() {
        Helper.setUser(null);
        UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
    }
}
