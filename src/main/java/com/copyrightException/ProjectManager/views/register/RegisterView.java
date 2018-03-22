package com.copyrightException.ProjectManager.views.register;

import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.login.LoginView;
import com.vaadin.data.HasValue;
import com.vaadin.data.HasValue.ValueChangeListener;
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

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

@SpringView(name = RegisterView.VIEW_NAME)
public class RegisterView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "register";
    private static final Logger LOG = LoggerFactory.getLogger(RegisterView.class);
    private final UserRepository userRepository;
    private Button bRegister, bBack;
    private Label lNameTitle, lPasswordTitle, lRepeatPasswordTitle, lErrorName, lErrorPassword, lErrorPasswordRepeat;
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

        tfName.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent event) {
                lErrorName.setVisible(false);
            }
        });
        pfPassword.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent event) {
                lErrorPassword.setVisible(false);
            }
        });
        pfRepeatPassword.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent event) {
                lErrorPasswordRepeat.setVisible(false);
            }
        });

        lErrorName = new Label();
        lErrorPassword = new Label();
        lErrorPasswordRepeat = new Label();

        lErrorName.setVisible(false);
        lErrorPassword.setVisible(false);
        lErrorPasswordRepeat.setVisible(false);

        bBack.setSizeFull();
        bRegister.setSizeFull();
        lErrorName.setSizeFull();
        lErrorPassword.setSizeFull();
        lErrorPasswordRepeat.setSizeFull();
    }

    private void initLayout() {
        GridLayout grid = new GridLayout(4, 6);
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

        grid.addComponent(lErrorName, 2, 1);
        grid.setComponentAlignment(lErrorName, Alignment.MIDDLE_CENTER);
        grid.addComponent(lErrorPassword, 2, 2);
        grid.setComponentAlignment(lErrorPassword, Alignment.MIDDLE_CENTER);
        grid.addComponent(lErrorPasswordRepeat, 2, 3);
        grid.setComponentAlignment(lErrorPasswordRepeat, Alignment.MIDDLE_CENTER);

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

        if (tfName.isEmpty()) {
            lErrorName.setVisible(tfName.isEmpty());
            lErrorName.setValue("please enter a username");
            return;
        }

        if (pfPassword.isEmpty()) {
            lErrorPassword.setVisible(pfPassword.isEmpty());
            lErrorPassword.setValue("please enter a password");
            return;
        }

        if (pfRepeatPassword.isEmpty()) {
            lErrorPasswordRepeat.setVisible(pfRepeatPassword.isEmpty());
            lErrorPasswordRepeat.setValue("please repeat your password");
            return;
        }

        if (!pfPassword.getValue().equals(pfRepeatPassword.getValue())) {
            lErrorPasswordRepeat.setVisible(true);
            lErrorPasswordRepeat.setValue("the passwords don't match");
            return;
        }

        if (!userRepository.findByName(tfName.getValue()).isEmpty()) {
            lErrorName.setVisible(tfName.isEmpty());
            lErrorName.setValue("this username already exists");
            return;
        }

        lErrorName.setVisible(true);
        lErrorPassword.setVisible(true);
        lErrorPasswordRepeat.setVisible(true);

        User user = new User();
        user.setName(tfName.getValue());

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] hashedPW = digestSHA3.digest(pfPassword.getValue().getBytes());

        System.out.println("SHA3-512 = " + Hex.toHexString(hashedPW));

        user.setPasswortHash(Hex.toHexString(hashedPW));
        userRepository.save(user);
        UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
    }

}
