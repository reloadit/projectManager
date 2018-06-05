package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Task;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TaskComponent extends Panel {

    private final Task task;
    private final TaskChangeListener taskChangeListener;
    private final Button bEdit = new Button();
    private final Button bDelete = new Button();
    private final Label laName = new Label();
    private final Label ladescription = new Label();
    private final Label laUser = new Label();

    public TaskComponent(Task task, TaskChangeListener taskChangeListener) {
        this.task = task;
        this.taskChangeListener = taskChangeListener;
        initLayout();
        initUi();
    }

    private void initLayout() {
        final HorizontalLayout nameLayout = new HorizontalLayout(laName, bEdit, bDelete);
        nameLayout.setExpandRatio(laName, 1);
        nameLayout.setWidth("100%");
        final VerticalLayout layout = new VerticalLayout(nameLayout, ladescription, laUser);
        setContent(layout);
    }

    private void initUi() {
        bEdit.setIcon(VaadinIcons.PENCIL);
        bEdit.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bEdit.addStyleName(ValoTheme.BUTTON_SMALL);
        bEdit.addClickListener(event -> taskChangeListener.editTask(task));

        bDelete.setIcon(VaadinIcons.MINUS_CIRCLE);
        bDelete.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
        bDelete.addStyleName(ValoTheme.BUTTON_SMALL);
        bDelete.addClickListener(event -> taskChangeListener.deleteTask(task));

        laName.setContentMode(ContentMode.HTML);
        laName.setValue(task.getName());

        if (task.getDone()) {
            laName.addStyleName("v-text-lineThrough");
            ladescription.addStyleName("v-text-lineThrough");
            laUser.addStyleName("v-text-lineThrough");
            addStyleName("v-task-done-bg");
        }

        laName.setWidth("100%");
        ladescription.setValue(task.getDescription());
        laUser.setValue(task.getAssignedUser() != null
                ? task.getAssignedUser().getName()
                : "");
    }

    public static interface TaskChangeListener {

        public void editTask(final Task task);

        public void deleteTask(final Task task);
    }
}
