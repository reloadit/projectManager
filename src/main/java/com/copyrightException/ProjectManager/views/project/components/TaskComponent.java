package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Task;
import com.vaadin.ui.VerticalLayout;

public class TaskComponent extends VerticalLayout {

    private final Task task;
    private final TaskChangeListener taskChangeListener;

    public TaskComponent(Task task, TaskChangeListener taskChangeListener) {
        this.task = task;
        this.taskChangeListener = taskChangeListener;
    }

    public static interface TaskChangeListener {

    }
}
