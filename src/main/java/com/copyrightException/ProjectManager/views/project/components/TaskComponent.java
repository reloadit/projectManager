package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Task;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TaskComponent extends Panel {

    private final Task task;
    private final TaskChangeListener taskChangeListener;
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
        final HorizontalLayout nameLayout = new HorizontalLayout(laName, laUser);
        nameLayout.setExpandRatio(laName, 3);
        nameLayout.setExpandRatio(laUser, 1);
        nameLayout.setComponentAlignment(laUser, Alignment.TOP_RIGHT);
        nameLayout.setWidth("100%");

        final VerticalLayout layout = new VerticalLayout(nameLayout, ladescription);
        setContent(layout);
    }

    private void initUi() {
        bDelete.setIcon(VaadinIcons.MINUS_CIRCLE);
        bDelete.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
        bDelete.addStyleName(ValoTheme.BUTTON_SMALL);
        bDelete.addClickListener(event -> onDelete());

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
        laUser.addStyleName("v-blueCircleBG-whiteText-12px");

        laName.setWidth("100%");
        ladescription.setWidth("100%");
        ladescription.setValue(task.getDescription());

        if (task.getAssignedUser() != null) {
            laUser.setValue(task.getAssignedUser().abbrvname());
            laUser.setVisible(true);
        } else {
            laUser.setValue("");
            laUser.setVisible(false);
        }
    }

    public void editTask() {
        taskChangeListener.editTask(task);
    }

    private void onDelete() {
        final ConfirmWindow confirmWindow = new ConfirmWindow(() -> taskChangeListener.deleteTask(task),
                 "Delete slot",
                String.format("Are you sure that you want to delete the slot: %s", task.getName()));
        UI.getCurrent().addWindow(confirmWindow);
    }

    public static interface TaskChangeListener {

        public void editTask(final Task task);

        public void deleteTask(final Task task);
    }
}
