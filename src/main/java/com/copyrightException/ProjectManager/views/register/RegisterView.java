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
    private Label lNameTitle, lPasswordTitle, lRepeatPasswordTitle;
    private TextField tfName;
    private PasswordField pfPassword, pfRepeatPassword;

    @Autowired
    public RegisterView(final UserRepository userRepository) {
        this.userRepository = userRepository;
        createComponents();
        initLayout();
        initUi();
    }

    private void createComponents() {
        lNameTitle = new Label("Username:");
        lPasswordTitle = new Label("Password:");
        lRepeatPasswordTitle = new Label("Repeat Password:");
        tfName = new TextField();
        pfPassword = new PasswordField();
        pfRepeatPassword = new PasswordField();
        bBack = new Button("Back");
        bRegister = new Button("Register");

        bBack.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);

        bBack.setIcon(VaadinIcons.ARROW_LEFT);

        tfName.setPlaceholder("enter here");
        pfPassword.setPlaceholder("enter here");
        pfRepeatPassword.setPlaceholder("enter here");

        tfName.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
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
        GridLayout grid = new GridLayout(4, 6);
        grid.setSpacing(true);
        grid.addComponent(bBack, 1, 0);
        grid.addComponent(lNameTitle, 0, 2); //-> (x, y)
        grid.setComponentAlignment(lNameTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(tfName, 1, 2);
        grid.addComponent(lPasswordTitle, 0, 3);
        grid.setComponentAlignment(lPasswordTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(pfPassword, 1, 3);
        grid.addComponent(lRepeatPasswordTitle, 0, 4);
        grid.setComponentAlignment(lRepeatPasswordTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(pfRepeatPassword, 1, 4);
        grid.addComponent(bRegister, 1, 5);

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

        if (tfName.isEmpty()) {
            Helper.displayErrorMessage("Empty Username", "Please enter a username", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
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

        if (!userRepository.findByName(tfName.getValue()).isEmpty()) {
            Helper.displayErrorMessage("Username Unavailable", "This username already exists", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }
        User user = new User();
        user.setName(tfName.getValue());

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] hashedPW = digestSHA3.digest(pfPassword.getValue().getBytes());

        user.setPasswortHash(Hex.toHexString(hashedPW));
        userRepository.save(user);
        UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
    }

}
