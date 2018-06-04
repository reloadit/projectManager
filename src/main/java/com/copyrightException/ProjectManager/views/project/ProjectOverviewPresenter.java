package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectOverviewPresenter {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectOverviewPresenter.class);
    private ProjectOverview view = null;

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectOverviewPresenter(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public void setView(ProjectOverview view) {
        this.view = view;
    }

    public void onViewEnter() {
        loadProjects();
    }

    public void onCreateProject(final Project project) {
        LOG.info(String.format("Create project: %s", project.getName()));
        final User currentUser = Helper.getUser();
        project.setCreator(currentUser);
        projectRepository.saveAndFlush(project);
        loadProjects();
    }

    public void onEditUserProfile(final User user) {
        LOG.info(String.format("Save user: %s", user.getName()));
        userRepository.saveAndFlush(user);
    }

    public void onEditProject(final Project project) {
        LOG.info(String.format("Save project: %s", project.getName()));
        projectRepository.saveAndFlush(project);
    }

    private void loadProjects() {
        view.setProjects(projectRepository.findAll());
    }
}
