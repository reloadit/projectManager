package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.views.project.components.ProjectCreationWindow;
import com.copyrightException.ProjectManager.views.project.components.ProjectOverviewHeader;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
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
    private final ProjectOverviewHeader header = new ProjectOverviewHeader();
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
        initUi();
        presenter.setView(this);
    }

    private void initLayout() {
        setSizeFull();
        addComponent(header);
        addComponent(gProject);
        setExpandRatio(gProject, 1f);
        gProject.setSizeFull();
    }

    private void initGrid() {
        gProject.addColumn(Project::getName).setCaption("Project");
        gProject.addColumn(project -> project.getCreator().getName()).setCaption("Creator");
        gProject.addColumn(p -> "Open", new ButtonRenderer(event -> openProject(event.getItem())))
                .setCaption("Open");
    }

    private void initUi() {
        header.setCreateProjectCallback(this::showCreateProjectWindow);
    }

    public void setProjects(final List<Project> projects) {
        gProject.setDataProvider(new ListDataProvider<>(projects));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        presenter.onViewEnter();
    }

    private void openProject(final Object obj) {
        LOG.info("openProject clicked");
        if (obj instanceof Project) {
            final Project project = (Project) obj;
            LOG.info(String.format("opening: %s", project.getName()));
            final String projectId = project.getId();
            UI.getCurrent().getNavigator().navigateTo(ProjectView.VIEW_NAME + "/project=" + projectId);
        }
    }

    private void showCreateProjectWindow() {
        LOG.info("onCreateProject");
        final ProjectCreationWindow window = new ProjectCreationWindow(presenter::onCreateProject);
        window.center();
        window.setModal(true);
        window.setVisible(true);
        UI.getCurrent().addWindow(window);
    }

}
