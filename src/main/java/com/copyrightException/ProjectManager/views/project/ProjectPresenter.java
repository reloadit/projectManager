package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.ProjecManagerEventBus;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.Slot;
import com.copyrightException.ProjectManager.entities.Task;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.SlotRepository;
import com.copyrightException.ProjectManager.repositories.TaskRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.project.components.SlotComponent;
import com.copyrightException.ProjectManager.views.project.components.TaskComponent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.UI;
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
            ProjecManagerEventBus.EVENT_BUS.register(this);
            if (project == null) {
                view.projectNotFound();
            } else {
                this.project = project;
                project.getSlots().sort((s1, s2) -> Integer.compare(s1.getPosition(), s2.getPosition()));
                project.getSlots().forEach(slot
                        -> slot.getTasks().sort((t1, t2) -> Integer.compare(t1.getPosition(), t2.getPosition())));
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
        slotRepository.saveAndFlush(slot);
        fireChangeEvent();
    }

    @Override
    public void nameChanged(Slot slot, String name) {
        slot.setName(name);
        view.setProject(project);
        slotRepository.saveAndFlush(slot);
        fireChangeEvent();
    }

    @Override
    public void removeSlot(Slot slot) {
        final List<Slot> slots = project.getSlots();
        slots.remove(slot);
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).setPosition(i);
        }
        view.setProject(project);
        taskRepository.deleteInBatch(slot.getTasks());
        slotRepository.delete(slot);
        fireChangeEvent();
    }

    @Override
    public void addNewTask(Slot slot) {
        final Task task = new Task();
        task.setName("");
        task.setDescription("");
        view.showAddTaskDialog(true, task, userRepository.findAll(), t -> {
            t.setPosition(slot.getTasks().size());
            t.setSlot(slot);
            slot.getTasks().add(t);
            view.setProject(project);
            taskRepository.saveAndFlush(t);
            fireChangeEvent();
        });
    }

    @Override
    public void editTask(Task task) {
        view.showAddTaskDialog(false, task, userRepository.findAll(), t -> {
            view.setProject(project);
            taskRepository.saveAndFlush(t);
            fireChangeEvent();
        });
    }

    public void swapSlot(final Slot slot1, final Slot slot2) {
        final int index1 = slot1.getPosition();
        final int index2 = slot2.getPosition();
        slot1.setPosition(index2);
        slot2.setPosition(index1);
        project.getSlots().sort((s1, s2) -> Integer.compare(s1.getPosition(), s2.getPosition()));
        slotRepository.save(slot1);
        slotRepository.save(slot2);
        slotRepository.flush();
        view.setProject(project);
        fireChangeEvent();
    }

    public void moveTask(final Task task, final Slot moveTarget) {
        LOG.info(String.format("Task: %s dropped on slot: %s#", task.getName(), moveTarget.getName()));
        final Slot oldSlot = task.getSlot();
        oldSlot.getTasks().remove(task);
        task.setSlot(moveTarget);
        moveTarget.getTasks().add(task);
        project.getSlots().forEach(slot -> {
            for (int i = 0; i < slot.getTasks().size(); i++) {
                slot.getTasks().get(i).setPosition(i);
            }
        });
        taskRepository.saveAndFlush(task);
        slotRepository.save(moveTarget);
        slotRepository.save(oldSlot);
        slotRepository.flush();
        view.setProject(project);
        fireChangeEvent();
    }

    @Override
    public void deleteTask(Task task) {
        final List<Task> tasks = task.getSlot().getTasks();
        tasks.remove(task);
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPosition(i);
        }
        taskRepository.delete(task);
        view.setProject(project);
        fireChangeEvent();
    }

    private void fireChangeEvent() {
        ProjecManagerEventBus.EVENT_BUS.post(project);
    }

    @Subscribe
    public void projectChanged(final Project changedProject) {
        LOG.info("Project Changed event");
        if (this.project.getId().equals(changedProject.getId())) {
            LOG.info("My Project changed.");
            UI.getCurrent().access(() -> {
                this.project = changedProject;
                view.setProject(changedProject);
            });
        }

    }
}
