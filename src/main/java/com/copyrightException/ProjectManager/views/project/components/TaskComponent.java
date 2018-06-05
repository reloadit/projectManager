package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Task;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
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
        final HorizontalLayout nameLayout = new HorizontalLayout(laName, bEdit, bDelete, laUser);
        nameLayout.setExpandRatio(laName, 8);
        nameLayout.setExpandRatio(bEdit, 1);
        nameLayout.setExpandRatio(bDelete, 1);
        nameLayout.setExpandRatio(laUser, 2);
        nameLayout.setWidth("100%");
        nameLayout.setComponentAlignment(laName, Alignment.TOP_CENTER);
        nameLayout.setComponentAlignment(bEdit, Alignment.TOP_CENTER);
        nameLayout.setComponentAlignment(bDelete, Alignment.TOP_CENTER);
        nameLayout.setComponentAlignment(laUser, Alignment.TOP_CENTER);

        final VerticalLayout layout = new VerticalLayout(nameLayout, ladescription);
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

        laName.addStyleName("v-text-bold-17px");
        ladescription.addStyleName("v-text-light-14px");
        laUser.addStyleName("v-blueCircleBG-whiteText-13px");

        laName.setWidth("100%");
        ladescription.setValue(task.getDescription());
        laUser.setValue(task.getAssignedUser() != null
                ? task.getAssignedUser().abbrvname()
                : "");
    }

    public static interface TaskChangeListener {

        public void editTask(final Task task);

        public void deleteTask(final Task task);
    }
}
