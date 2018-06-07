package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.SlotRepository;
import com.copyrightException.ProjectManager.repositories.TaskRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.project.components.EditProjectWindow;
import com.copyrightException.ProjectManager.views.project.components.Header;
import com.copyrightException.ProjectManager.views.project.components.ProjectCreationWindow;
import com.copyrightException.ProjectManager.views.project.components.ProjectOverviewHeader;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
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
    public static final String VIEW_NAME = "projectOverview";
    private final ProjectOverviewPresenter presenter;
    private final ProjectOverviewHeader projectOverviewHeader = new ProjectOverviewHeader();
    private final Header header;
    private final Grid<Project> gProject = new Grid<>();
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectOverview(
            final ProjectRepository projectRepository,
            final TaskRepository taskRepository,
            final SlotRepository slotRepository,
            final UserRepository userRepository) {
        presenter = new ProjectOverviewPresenter(projectRepository, taskRepository, slotRepository, userRepository);
        header = new Header(presenter::onEditUserProfile);
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
        setMargin(false);

        header.addStyleName("v-white55PercentBG");
        projectOverviewHeader.addStyleName("v-white55PercentBG");

        addStyleName("v-image-transparent-projectView-BG");
        addComponent(header);
        addComponent(projectOverviewHeader);
        addComponent(gProject);
        setExpandRatio(gProject, 1f);
        gProject.setSizeFull();
        gProject.addStyleName("v-transparentBG");
        gProject.addStyleName("v-text-bold-19px");
    }

    private void initGrid() {
        gProject.addColumn(Project::getName).setCaption("Project").setExpandRatio(4).setStyleGenerator(item -> gridTextStyle());
        gProject.addColumn(project -> project.getCreator().getName()).setCaption("Creator").setExpandRatio(4).setStyleGenerator(item -> gridTextStyle());
        gProject.addColumn(p -> "Show Details", new ButtonRenderer(event -> openProject(event.getItem()))).setExpandRatio(1).setStyleGenerator(item -> openProjectStyle((Project) item));
        gProject.addColumn(p -> "Edit", new ButtonRenderer(event -> editProject(event.getItem()))).setExpandRatio(1).setStyleGenerator(item -> editProjectStyle((Project) item));
    }

    private String editProjectStyle(final Project project) {
        return Helper.getUser().equals(project.getCreator())
                ? "pm-creator"
                : "pm-not-creator";
    }

    private String openProjectStyle(final Project project) {
        return project.isMember(Helper.getUser())
                ? "pm-member"
                : "pm-not-member";
    }

    private String gridTextStyle() {
        return "v-text-bold-17px";
    }

    private void initUi() {
        projectOverviewHeader.setCreateProjectCallback(this::showCreateProjectWindow);
    }

    public void setProjects(final List<Project> projects) {
        gProject.setDataProvider(new ListDataProvider<>(projects));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        presenter.onViewEnter();
        header.viewEnter();
    }

    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {
        View.super.beforeLeave(event);
        presenter.beforeLeave();
        header.viewLeave();
    }

    private void openProject(final Object obj) {
        LOG.info("openProject clicked");
        if (obj instanceof Project) {
            final Project project = (Project) obj;
            if (project.isMember(Helper.getUser())) {
                LOG.info(String.format("opening: %s", project.getName()));
                final String projectId = project.getId();
                UI.getCurrent().getNavigator().navigateTo(ProjectView.VIEW_NAME + "/project=" + projectId);
            } else {
                Helper.displayErrorMessage("Cannot open project",
                        "You must be a member to open the project. Please ask the creator of the project to add you as a member",
                        Notification.Type.ERROR_MESSAGE,
                        Position.TOP_CENTER,
                        Page.getCurrent());
            }
        }
    }

    private void editProject(final Object obj) {
        LOG.info("editProject clicked");
        if (obj instanceof Project) {
            final Project project = (Project) obj;
            if (Helper.getUser().equals(project.getCreator())) {
                LOG.info(String.format("editing: %s", project.getName()));
                final EditProjectWindow window = new EditProjectWindow(false, presenter::onEditProject, presenter::onDeleteProject, project, userRepository);
                window.center();
                window.setModal(true);
                window.setVisible(true);
                UI.getCurrent().addWindow(window);
            } else {
                Helper.displayErrorMessage("Cannot edit project",
                        "You must be the creator of the project to edit it.",
                        Notification.Type.ERROR_MESSAGE,
                        Position.TOP_CENTER,
                        Page.getCurrent());
            }
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
