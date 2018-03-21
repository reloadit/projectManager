package com.copyrightException.ProjectManager.views.register;

import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.login.LoginView;
import com.copyrightException.ProjectManager.views.project.ProjectView;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

        tfName.setPlaceholder("enter here");
        pfPassword.setPlaceholder("enter here");
        pfRepeatPassword.setPlaceholder("enter here");

        bBack.setSizeFull();
        bRegister.setSizeFull();
    }

    private void initLayout() {
        GridLayout grid = new GridLayout(3, 6);
        grid.setSpacing(true);
        grid.addComponent(bBack, 1, 0);
        grid.addComponent(lNameTitle, 0, 1); //-> (x, y)
        grid.setComponentAlignment(lNameTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(tfName, 1, 1);
        grid.addComponent(lPasswordTitle, 0, 2);
        grid.setComponentAlignment(lPasswordTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(pfPassword, 1, 2);
        grid.addComponent(lRepeatPasswordTitle, 0, 3);
        grid.setComponentAlignment(lRepeatPasswordTitle, Alignment.MIDDLE_CENTER);
        grid.addComponent(pfRepeatPassword, 1, 3);
        grid.addComponent(bRegister, 1, 5);

        addComponent(grid);
        setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
    }

    private void initUi() {
        bBack.addClickListener(e -> onBack());
        bRegister.addClickListener(e -> onRegister());
    }

    private void onBack() {
        System.out.println("onBack");
        UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
    }

    private void onRegister() {
        System.out.println("onRegister");
        //TODO register user
    }
}
