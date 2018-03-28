package com.copyrightException.ProjectManager.views.login;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.project.ProjectOverview;
import com.copyrightException.ProjectManager.views.register.RegisterView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
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
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "";
    private static final Logger LOG = LoggerFactory.getLogger(LoginView.class);
    private final UserRepository userRepository;
    private Button bRegister, bLogin;
    private Label lNameTitle, lPasswordTitle;
    private TextField tfName;
    private PasswordField pfPassword;

    @Autowired
    public LoginView(final UserRepository userRepository) {
        this.userRepository = userRepository;
        createComponents();
        initLayout();
        initUi();
    }

    private void createComponents() {
        lNameTitle = new Label("Username:");
        lPasswordTitle = new Label("Password:");
        tfName = new TextField();
        pfPassword = new PasswordField();
        bLogin = new Button("Login");
        bRegister = new Button("Not registered yet?");

        bLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);

        tfName.setPlaceholder("enter here");
        pfPassword.setPlaceholder("enter here");

        tfName.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onLogin();
            }
        });

        pfPassword.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onLogin();
            }
        });

        bLogin.setSizeFull();
        bRegister.setSizeFull();
    }

    private void initLayout() {
        GridLayout grid = new GridLayout(3, 5);
        grid.setSpacing(true);
        grid.addComponent(lNameTitle, 0, 0); //-> (x, y)
        grid.setComponentAlignment(lNameTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(tfName, 1, 0);
        grid.addComponent(lPasswordTitle, 0, 1);
        grid.setComponentAlignment(lPasswordTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(pfPassword, 1, 1);
        grid.addComponent(bLogin, 1, 2);
        grid.addComponent(bRegister, 1, 4);

        addComponent(grid);
        setSizeFull();
        setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
    }

    private void initUi() {
        bLogin.addClickListener(e -> onLogin());
        bRegister.addClickListener(e -> onRegister());
    }

    private void onLogin() {
        if (tfName.isEmpty()) {
            Helper.displayErrorMessage("Empty Username", "Please enter a username", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (pfPassword.isEmpty()) {
            Helper.displayErrorMessage("Empty Password", "Please enter a password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (userRepository.findByName(tfName.getValue()).isEmpty()) {
            Helper.displayErrorMessage("Invalid Username", "This username does not exist", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (!userRepository.findByName(tfName.getValue()).isEmpty()) {
            SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
            byte[] enteredHashedPW = digestSHA3.digest(pfPassword.getValue().getBytes());

            User user = userRepository.findByName(tfName.getValue()).get(0);

            if (Hex.toHexString(enteredHashedPW).equals(user.getPasswortHash())) {
                Helper.setUser(user);
                UI.getCurrent().getNavigator().navigateTo(ProjectOverview.VIEW_NAME);
            } else {
                Helper.displayErrorMessage("Wrong Password", "Please enter the correct password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            }
        }

    }

    private void onRegister() {
        UI.getCurrent().getNavigator().navigateTo(RegisterView.VIEW_NAME);
    }
}
