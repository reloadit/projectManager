package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.views.login.LoginView;
import com.copyrightException.ProjectManager.views.project.ProjectOverview;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Header extends Panel {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectOverview.class);
    private Button bEditProfile, bLogout;
    private Label lName, lDate;
    private final Consumer<User> userChangeCallback;
    
    public Header( Consumer<User>  userChangeCallback) {
        super();
        this.userChangeCallback = userChangeCallback;
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
        bEditProfile = new Button("Edit Profile");
        bLogout = new Button("Logout");

        bEditProfile.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bLogout.addStyleName(ValoTheme.BUTTON_PRIMARY);

        bEditProfile.setIcon(VaadinIcons.EDIT);
        bLogout.setIcon(VaadinIcons.EXIT);
    }

    private void initLayout() {
        final GridLayout layout = new GridLayout(5, 1);
        layout.setSpacing(true);
        layout.addComponent(lName, 0, 0);
        layout.addComponent(lDate, 1, 0);
        layout.addComponent(bEditProfile, 3, 0);
        layout.addComponent(bLogout, 4, 0);
        layout.setComponentAlignment(lName, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(lDate, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(bEditProfile, Alignment.MIDDLE_RIGHT);
        layout.setComponentAlignment(bLogout, Alignment.MIDDLE_RIGHT);
        layout.setMargin(new MarginInfo(false, true, false, true));

        setContent(layout);
        layout.setSizeFull();
        layout.setHeight("60px");
    }

    private void initUi() {
        bEditProfile.addClickListener(e -> onEditProfile());
        bLogout.addClickListener(e -> onLogout());
    }

    private void onEditProfile() {
        LOG.info("onEditProfile");
        final EditProfileWindow window = new EditProfileWindow(userChangeCallback);
        window.center();
        window.setModal(true);
        window.setVisible(true);
        UI.getCurrent().addWindow(window);
    }

    private void onLogout() {
        LOG.info("onLogout");
        Helper.setUser(null);
        UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
    }
}
