package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Task;
import com.copyrightException.ProjectManager.entities.User;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
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
    private final CheckBox chbDone = new CheckBox();
    private final CheckBox chbDelete = new CheckBox();
    private final Consumer<Task> createProjectCallBack;
    private final Consumer<Task> deleteProjectCallBack;
    private final Binder<Task> binder = new Binder();
    private final Task task;
    private final boolean create;

    public TaskWindow(
            final boolean create,
            final Task task,
            final Consumer<Task> createProjectCallBack,
            final Consumer<Task> deleteProjectCallBack,
            final List<User> users) {
        super(create
                ? "Create task"
                : "Edit task");
        this.createProjectCallBack = createProjectCallBack;
        this.deleteProjectCallBack = deleteProjectCallBack;
        this.task = task;
        this.create = create;
        cbAssignedUser.setItems(users);
        initLayout();
        initUi(users);
        initBinder();
        binder.readBean(task);
    }

    private void initLayout() {
        final CssLayout doneCBCssLayout = new CssLayout(chbDone);
        doneCBCssLayout.setCaption("Mark as done");
        final CssLayout deleteCBCssLayout = new CssLayout(chbDelete);
        deleteCBCssLayout.setCaption("Delete task");
        final FormLayout formLayout = new FormLayout(tfName, tfDescription, cbAssignedUser, doneCBCssLayout);
        if (!create) {
            formLayout.addComponent(deleteCBCssLayout);
        }
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
        cbAssignedUser.setItemCaptionGenerator(user -> user.getName());
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
        binder.forField(chbDone)
                .bind(Task::getDone, Task::setDone);
    }

    private void onCreateProject() {
        try {
            if (chbDelete.getValue()) {
                final ConfirmWindow confirmWindow = new ConfirmWindow(() -> {
                    Helper.displayErrorMessage("Deleted task successfully", "The task \"" + task.getName() + "\" has been deleted successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
                    deleteProjectCallBack.accept(task);
                    this.close();
                },
                        "Delete Task",
                        String.format("Are you sure that you want to delete the task: %s", task.getName()));
                UI.getCurrent().addWindow(confirmWindow);
            } else {
                binder.writeBean(task);
                createProjectCallBack.accept(task);
                if (create) {
                    Helper.displayErrorMessage("Created task successfully", "The task \"" + task.getName() + "\" has been created successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
                } else {
                    Helper.displayErrorMessage("Updated task successfully", "The task \"" + task.getName() + "\" has been updated successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
                }
                this.close();
            }

        } catch (ValidationException ex) {
            LOG.error("Validation exception");
        }
    }

    private void onCancel() {
        this.close();
    }

}
