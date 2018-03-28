package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.SlotRepository;
import com.copyrightException.ProjectManager.repositories.TaskRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;

public class ProjectPresenter {

    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private ProjectView view;
    private Project project;

    public ProjectPresenter(
            final ProjectRepository projectRepository,
            final SlotRepository slotRepository,
            final TaskRepository taskRepository,
            final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public void setView(ProjectView view) {
        this.view = view;
    }

    public void onEnter(final String projectId) {
        if (projectId != null) {
            final Project project = projectRepository.findFirstById(projectId);
            if (project == null) {
                view.projectNotFound();
            } else {
                this.project = project;
                view.setProject(project);
            }
        } else {
            view.projectNotFound();
        }
    }
}
