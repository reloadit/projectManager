package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.ProjecManagerEventBus;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectOverviewPresenter {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectOverviewPresenter.class);
    private ProjectOverview view = null;

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private UI ui;

    public ProjectOverviewPresenter(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public void setView(ProjectOverview view) {
        this.view = view;
    }

    public void onViewEnter() {
        loadProjects();
        ui = UI.getCurrent();
        ProjecManagerEventBus.EVENT_BUS.register(this);
    }

    public void beforeLeave() {
        ProjecManagerEventBus.EVENT_BUS.unregister(this);
    }

    public void onCreateProject(final Project project) {
        LOG.info(String.format("Create project: %s", project.getName()));
        final User currentUser = Helper.getUser();
        project.setCreator(currentUser);
        projectRepository.saveAndFlush(project);
        fireProjectChangedEvent(project);
        loadProjects();
    }

    public void onEditUserProfile(final User user) {
        LOG.info(String.format("Save user: %s", user.getName()));
        userRepository.saveAndFlush(user);
    }

    public void onEditProject(final Project project) {
        LOG.info(String.format("Save project: %s", project.getName()));
        projectRepository.saveAndFlush(project);
        fireProjectChangedEvent(project);
    }

    private void loadProjects() {
        view.setProjects(projectRepository.findAll());
    }

    private void fireProjectChangedEvent(final Project project) {
        ProjecManagerEventBus.EVENT_BUS.post(project);
    }

    @Subscribe
    public void projectChanged(final Project project) {
        if (project.getId() != null) {
            ui.access(() -> {
                loadProjects();
            });
        }
    }

    @Subscribe
    public void userChanged(final User user) {
        ui.access(() -> {
            loadProjects();
        });
    }
}
