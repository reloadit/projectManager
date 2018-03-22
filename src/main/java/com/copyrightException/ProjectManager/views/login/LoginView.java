package com.copyrightException.ProjectManager.views.login;

import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.project.ProjectOverview;
import com.copyrightException.ProjectManager.views.register.RegisterView;
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

        tfName.setPlaceholder("enter here");
        pfPassword.setPlaceholder("enter here");

        //lNameTitle.setSizeFull();
        //lPasswordTitle.setSizeFull();
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
        setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
    }

    private void initUi() {
        bLogin.addClickListener(e -> onLogin());
        bRegister.addClickListener(e -> onRegister());
    }

    private void onLogin() {
        System.out.println("onLogin");
        UI.getCurrent().getNavigator().navigateTo(ProjectOverview.VIEW_NAME);
    }

    private void onRegister() {
        System.out.println("onRegister");
        UI.getCurrent().getNavigator().navigateTo(RegisterView.VIEW_NAME);
    }
}
