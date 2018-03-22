package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.SlotRepository;
import com.copyrightException.ProjectManager.repositories.TaskRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = ProjectView.VIEW_NAME)
public class ProjectView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "project";

    private final Label laProjectName = new Label();
    private final Panel paHeader = new Panel();
    private final Panel paSlots = new Panel();

    private final ProjectPresenter presenter;

    @Autowired
    public ProjectView(
            final ProjectRepository projectRepository,
            final SlotRepository slotRepository,
            final TaskRepository taskRepository,
            final UserRepository userRepository) {
        presenter = new ProjectPresenter(projectRepository, slotRepository, taskRepository, userRepository);
    }

    @PostConstruct
    public void init() {
        initLayout();
        initUi();
        presenter.setView(this);
    }

    private void initHeaderLayout() {

    }

    private void initLayout() {
        initHeaderLayout();
        setSizeFull();
        paHeader.setWidth("100%");
        addComponent(paHeader);
        addComponent(paSlots);
    }

    private void initUi() {

    }

    public void entityToGui(final Project project) {

    }

    public void guiToEntity(final Project project) {
        laProjectName.setValue(project.getName());
    }

}
