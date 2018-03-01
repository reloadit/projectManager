package com.copyrightException.ProjectManager.views;

import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = MainView.viewName)
public class MainView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(MainView.class);
    public static final String viewName = "";
    private final UserRepository userRepository;
    private Button bClickMe;

    @Autowired
    public MainView(final UserRepository userRepository) {
        this.userRepository = userRepository;
        createComponents();
        initLayout();
        initUi();
    }

    private void createComponents() {
        bClickMe = new Button("click me");
    }

    private void initLayout() {
        addComponent(bClickMe);
    }

    private void initUi() {
        bClickMe.addClickListener(e -> onClickMeClick());
    }

    private void onClickMeClick() {
        final User user = new User();
        user.setName("Test User");
        user.setPasswortHash("sdsd");
        userRepository.save(user);
    }
}
