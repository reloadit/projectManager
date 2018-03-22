package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectOverviewPresenter {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectOverviewPresenter.class);
    private ProjectOverview view = null;

    private final ProjectRepository projectRepository;

    public ProjectOverviewPresenter(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

    private void loadProjects() {
        view.setProjects(projectRepository.findAll());
    }
}
