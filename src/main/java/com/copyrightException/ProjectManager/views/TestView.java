package com.copyrightException.ProjectManager.views;

import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = TestView.viewName)
public class TestView extends VerticalLayout implements View {

    public static final String viewName = "test";
    private static final Logger LOG = LoggerFactory.getLogger(TestView.class);
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private Button bClickMe;

    @Autowired
    public TestView(final UserRepository userRepository, final ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
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
        LOG.info("Creating project");
        final Project project = new Project();
        project.setName("TestProject");
        final User user = userRepository.findAll().get(0);
        project.setCreator(user);
        projectRepository.save(project);
    }
}
