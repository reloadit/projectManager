package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.repositories.ProjectRepository;

public class ProjectOverviewPresenter {

    private ProjectOverview view = null;

    private final ProjectRepository projectRepository;

    public ProjectOverviewPresenter(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void setView(ProjectOverview view) {
        this.view = view;
    }

    public void onViewEnter() {
        view.setProjects(projectRepository.findAll());
    }

}
