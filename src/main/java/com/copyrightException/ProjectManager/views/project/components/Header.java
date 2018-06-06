package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.ProjecManagerEventBus;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.views.login.LoginView;
import com.copyrightException.ProjectManager.views.project.ProjectOverview;
import com.google.common.eventbus.Subscribe;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
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
    private UI ui;

    public Header(Consumer<User> userChangeCallback) {
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

        lName.addStyleName("v-text-bold-19px");
        lDate.addStyleName("v-text-bold-17px");
        bEditProfile.addStyleName("v-text-bold-17px");
        bLogout.addStyleName("v-text-bold-17px");
        bEditProfile.addStyleName("v-text-bold-17px");

        bEditProfile.setIcon(VaadinIcons.EDIT);
        bLogout.setIcon(VaadinIcons.EXIT);
    }

    private void initLayout() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSizeFull();
        layout.addComponent(lName);
        layout.addComponent(lDate);
        layout.addComponent(bEditProfile);
        layout.addComponent(bLogout);
        layout.setComponentAlignment(lName, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(lDate, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(bEditProfile, Alignment.MIDDLE_RIGHT);
        layout.setComponentAlignment(bLogout, Alignment.MIDDLE_RIGHT);

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

    public void viewEnter() {
        ui = UI.getCurrent();
        ProjecManagerEventBus.EVENT_BUS.register(this);
    }

    public void viewLeave() {
        ProjecManagerEventBus.EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void userChanged(final User user) {
        ui.access(() -> {
            if (user.equals(Helper.getUser())) {
                lName.setValue("Hello, " + user.getName() + "!");
            }
        });
    }
}
