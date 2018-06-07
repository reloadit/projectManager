package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditProjectWindow extends Window {

    private static final Logger LOG = LoggerFactory.getLogger(EditProjectWindow.class);
    private final Button bSave = new Button();
    private final Button bCancel = new Button();
    private TextField tfProjectName;
    private Grid<User> selectMemberGrid;
    private final UserRepository userRepository;
    private final Consumer<Project> saveProjectCallBack;
    private final CheckBox chbDelete = new CheckBox();
    private Project project;
    private List<User> members = new ArrayList();
    private final Consumer<Project> deleteCallback;
    private final boolean create;

    public EditProjectWindow(final boolean create, final Consumer<Project> saveProjectCallBack, final Consumer<Project> deleteCallback, final Project project, final UserRepository userRepository) {
        super(create
                ? "Create project"
                : "Edit project");
        this.create = create;
        this.deleteCallback = deleteCallback;
        this.saveProjectCallBack = saveProjectCallBack;
        this.project = project;
        this.userRepository = userRepository;
        initUi();
        initLayout();
    }

    private void initLayout() {
        final HorizontalLayout buttonLayout = new HorizontalLayout(bSave, bCancel);
        final VerticalLayout layout = new VerticalLayout(tfProjectName, selectMemberGrid, chbDelete, buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
        layout.setComponentAlignment(chbDelete, Alignment.BOTTOM_LEFT);
        layout.setWidthUndefined();
        buttonLayout.setWidthUndefined();
        this.setWidthUndefined();
        this.setContent(layout);
    }

    private void initUi() {
        tfProjectName = new TextField();
        tfProjectName.setCaption("Project Name");
        chbDelete.setCaption("Delete Project");
        chbDelete.setSizeFull();
        selectMemberGrid = new Grid<>();
        selectMemberGrid.setSelectionMode(SelectionMode.MULTI);
        List<User> allUser = new ArrayList<>();
        allUser.addAll(userRepository.findAll());
        Predicate<User> myUserPredicate = u -> u.getId().equals(Helper.getUser().getId());
        allUser.removeIf(myUserPredicate);
        selectMemberGrid.setItems(allUser);
        selectMemberGrid.addColumn(User::getName).setCaption("User Name");
        selectMemberGrid.addColumn(User::getFirstName).setCaption("First Name");
        selectMemberGrid.addColumn(User::getLastName).setCaption("Last Name");

        bSave.setCaption(create
                ? "Create"
                : "Save");
        bSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bSave.addClickListener(event -> onSaveProject());

        bCancel.setCaption("Cancel");
        bCancel.addClickListener(event -> onCancel());

        if (project.getName() != null) {
            tfProjectName.setValue(project.getName());
        }

        for (int i = 0; i < allUser.size(); i++) {
            if (project.getUsers().contains(allUser.get(i))) {
                selectMemberGrid.select(allUser.get(i));
            }
        }

        tfProjectName.focus();
        tfProjectName.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProject();
            }
        });

        this.setResizable(false);
    }

    private void onSaveProject() {

        if (chbDelete.getValue()) {
            final ConfirmWindow confirmWindow = new ConfirmWindow(() -> {
                this.close();
                deleteCallback.accept(project);
            },
                    "Delete Projekt",
                    String.format("Are you sure that you want to delete the projekt: %s", project.getName()));

            UI.getCurrent().addWindow(confirmWindow);
        } else {
            if (tfProjectName.isEmpty()) {
                Helper.displayErrorMessage("Empty Project Name", "Please enter a name for the project", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
                return;
            }

            members.clear();
            members.addAll(selectMemberGrid.getSelectedItems());

            project.setName(tfProjectName.getValue());
            project.setUsers(members);

            saveProjectCallBack.accept(project);

            this.close();

            if (create) {
                Helper.displayErrorMessage("Created project successfully", "The project \"" + project.getName() + "\" has been created successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
            } else {
                Helper.displayErrorMessage("Updated project successfully", "The project \"" + project.getName() + "\" has been updated successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
            }
        }
    }

    private void onCancel() {
        this.close();
    }

}
