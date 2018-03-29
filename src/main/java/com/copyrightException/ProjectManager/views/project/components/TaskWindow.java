package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Task;
import com.copyrightException.ProjectManager.entities.User;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskWindow extends Window {

    private static final Logger LOG = LoggerFactory.getLogger(SlotCreationWindow.class);
    private final TextField tfDescription = new TextField();
    private final ComboBox<User> cbAssignedUser = new ComboBox<>();
    private final Button bSave = new Button();
    private final Button bCancel = new Button();
    private final TextField tfName = new TextField();
    private final Consumer<Task> taskCallback;
    private final Binder<Task> binder = new Binder();
    private final Task task;
    private final boolean create;

    public TaskWindow(
            final boolean create,
            final Task task,
            final Consumer<Task> createProjectCallBack,
            final List<User> users) {
        super(create
                ? "Create task"
                : "Edit task");
        this.taskCallback = createProjectCallBack;
        this.task = task;
        this.create = create;
        initLayout();
        initUi(users);
        initBinder();
        binder.readBean(task);
    }

    private void initLayout() {
        final FormLayout formLayout = new FormLayout(tfName, tfDescription, cbAssignedUser);
        final HorizontalLayout buttonLayout = new HorizontalLayout(bSave, bCancel);
        final VerticalLayout layout = new VerticalLayout(formLayout, buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
        layout.setWidthUndefined();
        buttonLayout.setWidthUndefined();
        formLayout.setWidthUndefined();
        this.setWidthUndefined();
        this.setContent(layout);
    }

    private void initUi(final List<User> users) {
        tfName.setCaption("Task name");
        tfDescription.setCaption("Description");
        cbAssignedUser.setCaption("Assigned user");
        bSave.setCaption(create
                ? "Create"
                : "Save");
        bSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bSave.addClickListener(event -> onCreateProject());

        bCancel.setCaption("Cancel");
        bCancel.addClickListener(event -> onCancel());

        tfName.addShortcutListener(new ShortcutListener("Save task", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onCreateProject();
            }
        });
        tfName.focus();

        this.setResizable(false);
    }

    private void initBinder() {
        binder.forField(tfName)
                .withValidator(new StringLengthValidator("Task name must be between 1 and 255 characters long", 1, 255))
                .bind(Task::getName, Task::setName);
        binder.forField(tfDescription)
                .withValidator(new StringLengthValidator("Task description must less than 1024 characters long", 0, 1024))
                .bind(Task::getDescription, Task::setDescription);
        binder.forField(cbAssignedUser)
                .bind(Task::getAssignedUser, Task::setAssignedUser);
    }

    private void onCreateProject() {
        try {
            binder.writeBean(task);
            taskCallback.accept(task);
            this.close();
        } catch (ValidationException ex) {
            LOG.error("Validation exception");
        }
    }

    private void onCancel() {
        this.close();
    }

}
