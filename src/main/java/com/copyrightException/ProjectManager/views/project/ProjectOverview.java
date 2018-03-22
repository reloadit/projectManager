package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = ProjectOverview.VIEW_NAME)
public class ProjectOverview extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectOverview.class);
    public static final String VIEW_NAME = "projectOveriew";
    private final ProjectOverviewPresenter presenter;
    private final Grid<Project> gProject = new Grid<>();
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectOverview(final ProjectRepository projectRepository) {
        presenter = new ProjectOverviewPresenter(projectRepository);
        this.projectRepository = projectRepository;
    }

    @PostConstruct
    public void init() {
        initGrid();
        initLayout();
        presenter.setView(this);
    }

    private void initLayout() {
        addComponent(gProject);
    }

    private void initGrid() {
        gProject.addColumn(Project::getName).setCaption("Project");
        gProject.addColumn(project -> project.getCreator().getName()).setCaption("Creator");
        gProject.addColumn(this::createButton, new ButtonRenderer()).setCaption("Open");
    }

    public void setProjects(final List<Project> projects) {
        gProject.setDataProvider(new ListDataProvider<>(projects));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        presenter.onViewEnter();
    }

    private Button createButton(final Project project) {
        final Button button = new Button();
        button.addClickListener(event -> openProject(project));
        button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        button.setIcon(VaadinIcons.ARROW_CIRCLE_RIGHT);
        return button;
    }

    private void openProject(final Project project) {
        LOG.info("openProject clicked");
    }

}
