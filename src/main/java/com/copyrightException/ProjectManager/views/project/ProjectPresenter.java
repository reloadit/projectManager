package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.Slot;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.SlotRepository;
import com.copyrightException.ProjectManager.repositories.TaskRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.project.components.SlotComponent;
import com.copyrightException.ProjectManager.views.project.components.TaskComponent;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectPresenter implements SlotComponent.SlotChangeListener, TaskComponent.TaskChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectPresenter.class);
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

    public void addSlot(final Slot slot) {
        LOG.info("addSlot()");
        slot.setProject(project);
        slot.setPosition(project.getSlots().size());
        slot.setTasks(new ArrayList<>());
        project.getSlots().add(slot);
        view.setProject(project);
    }

    @Override
    public void nameChanged(Slot slot, String name) {
        slot.setName(name);
        view.setProject(project);
    }

    @Override
    public void removeSlot(Slot slot) {
        final List<Slot> slots = project.getSlots();
        slots.remove(slot);
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).setPosition(i);
        }
        view.setProject(project);
    }

}
