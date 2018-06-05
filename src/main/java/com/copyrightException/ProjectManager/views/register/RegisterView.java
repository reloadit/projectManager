package com.copyrightException.ProjectManager.views.register;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.login.LoginView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

@SpringView(name = RegisterView.VIEW_NAME)
public class RegisterView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "register";
    private static final Logger LOG = LoggerFactory.getLogger(RegisterView.class);
    private final UserRepository userRepository;
    private Button bRegister, bBack;
    private Label lUsernameTitle, lFirstNameTitle, lLastNameTitle, lPasswordTitle, lRepeatPasswordTitle;
    private TextField tfUserName;
    private TextField tfFirstname = new TextField();
    private TextField tfLastname = new TextField();
    private PasswordField pfPassword, pfRepeatPassword;

    @Autowired
    public RegisterView(final UserRepository userRepository) {
        this.userRepository = userRepository;
        createComponents();
        initLayout();
        initUi();
    }

    private void createComponents() {
        tfUserName = new TextField();
        tfFirstname = new TextField();
        tfLastname = new TextField();

        lUsernameTitle = new Label("Username:");
        lFirstNameTitle = new Label("First name:");
        lLastNameTitle = new Label("Last name:");
        lPasswordTitle = new Label("Password:");
        lRepeatPasswordTitle = new Label("Repeat Password:");
        pfPassword = new PasswordField();
        pfRepeatPassword = new PasswordField();
        bBack = new Button("Back");
        bRegister = new Button("Register");

        tfUserName.focus();

        bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);

        bBack.setIcon(VaadinIcons.ARROW_LEFT);

        tfUserName.addStyleName("v-textfield-bold-17px");
        tfFirstname.addStyleName("v-textfield-bold-17px");
        tfLastname.addStyleName("v-textfield-bold-17px");
        lUsernameTitle.addStyleName("v-textfield-bold-17px");
        lFirstNameTitle.addStyleName("v-textfield-bold-17px");
        lLastNameTitle.addStyleName("v-textfield-bold-17px");
        lPasswordTitle.addStyleName("v-textfield-bold-17px");
        lRepeatPasswordTitle.addStyleName("v-textfield-bold-17px");
        pfPassword.addStyleName("v-textfield-bold-17px");
        pfRepeatPassword.addStyleName("v-textfield-bold-17px");
        bBack.addStyleName("v-textfield-bold-17px");
        bRegister.addStyleName("v-textfield-bold-17px");

        tfUserName.setPlaceholder("enter here");
        tfFirstname.setPlaceholder("enter here");
        tfLastname.setPlaceholder("enter here");
        pfPassword.setPlaceholder("enter here");
        pfRepeatPassword.setPlaceholder("enter here");

        tfUserName.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onRegister();
            }
        });

        tfFirstname.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onRegister();
            }
        });

        tfLastname.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onRegister();
            }
        });

        pfPassword.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onRegister();
            }
        });

        pfRepeatPassword.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onRegister();
            }
        });

        bBack.setSizeFull();
        bRegister.setSizeFull();
    }

    private void initLayout() {
        GridLayout grid = new GridLayout(4, 9);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.addComponent(bBack, 1, 0);
        grid.addComponent(lUsernameTitle, 0, 2);
        grid.addComponent(lFirstNameTitle, 0, 3);
        grid.addComponent(lLastNameTitle, 0, 4);
        grid.setComponentAlignment(lUsernameTitle, Alignment.MIDDLE_RIGHT);
        grid.setComponentAlignment(lFirstNameTitle, Alignment.MIDDLE_RIGHT);
        grid.setComponentAlignment(lLastNameTitle, Alignment.MIDDLE_RIGHT);
        grid.addComponent(tfUserName, 1, 2);
        grid.addComponent(tfFirstname, 1, 3);
        grid.addComponent(tfLastname, 1, 4);
        grid.addComponent(lPasswordTitle, 0, 5);
        grid.setComponentAlignment(lPasswordTitle, Alignment.MIDDLE_RIGHT);
        grid.addComponent(pfPassword, 1, 5);
        grid.addComponent(lRepeatPasswordTitle, 0, 6);
        grid.setComponentAlignment(lRepeatPasswordTitle, Alignment.MIDDLE_RIGHT);
        grid.addComponent(pfRepeatPassword, 1, 6);
        grid.addComponent(bRegister, 1, 8);

        grid.addStyleName("v-white65PercentBG-roundCorners");

        addStyleName("v-imageBG-transparentBG");

        addComponent(grid);
        setSizeFull();
        setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
    }

    private void initUi() {
        bBack.addClickListener(e -> onBack());
        bRegister.addClickListener(e -> onRegister());
    }

    private void onBack() {
        LOG.info("onBack");
        UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
    }

    private void onRegister() {
        LOG.info("onRegister");

        if (tfUserName.isEmpty()) {
            Helper.displayErrorMessage("Empty Username", "Please enter a username", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (tfFirstname.isEmpty()) {
            Helper.displayErrorMessage("Empty First name", "Please enter a first name", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (tfLastname.isEmpty()) {
            Helper.displayErrorMessage("Empty Last name", "Please enter a last name", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (pfPassword.isEmpty()) {
            Helper.displayErrorMessage("Empty Password", "Please enter a password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (pfPassword.getValue().length() < 10) {
            Helper.displayErrorMessage("Password Length Insufficient", "Please enter a password with at least 10 characters", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (pfRepeatPassword.isEmpty()) {
            Helper.displayErrorMessage("Empty Password", "Please repeat your password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (!pfPassword.getValue().equals(pfRepeatPassword.getValue())) {
            Helper.displayErrorMessage("Passwords Unequal", "The passwords don`t match", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (!userRepository.findByName(tfUserName.getValue()).isEmpty()) {
            Helper.displayErrorMessage("Username Unavailable", "This username already exists", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }
        User user = new User();
        user.setName(tfUserName.getValue());
        user.setFirstName(tfFirstname.getValue());
        user.setLastName(tfLastname.getValue());

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] hashedPW = digestSHA3.digest(pfPassword.getValue().getBytes());

        user.setPasswortHash(Hex.toHexString(hashedPW));
        userRepository.save(user);

        UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
        Helper.displayErrorMessage("Registration successful", "The Registration for the user \"" + user.getName() + "\" has been successful", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
    }

}
