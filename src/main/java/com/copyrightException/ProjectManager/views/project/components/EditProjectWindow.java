package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
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
    private Project project;
    private List<User> members = new ArrayList();

    public EditProjectWindow(final Consumer<Project> saveProjectCallBack, final Project project, final UserRepository userRepository) {
        super("Edit User Profile");
        this.saveProjectCallBack = saveProjectCallBack;
        this.project = project;
        this.userRepository = userRepository;
        initUi();
        initLayout();
    }

    private void initLayout() {
        final HorizontalLayout buttonLayout = new HorizontalLayout(bSave, bCancel);
        final VerticalLayout layout = new VerticalLayout(tfProjectName, selectMemberGrid, buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
        layout.setWidthUndefined();
        buttonLayout.setWidthUndefined();
        this.setWidthUndefined();
        this.setContent(layout);
    }

    private void initUi() {
        tfProjectName = new TextField();
        tfProjectName.setCaption("Project Name");

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

        bSave.setCaption("Save");
        bSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bSave.addClickListener(event -> onSaveProject());

        bCancel.setCaption("Cancel");
        bCancel.addClickListener(event -> onCancel());

        if (project.getName() != null) {
            tfProjectName.setValue(project.getName());
        }

        for (int i = 0; i < allUser.size(); i++) {
            System.out.println("for success --- " + project.getUsers().size() + " - " + allUser.size());
            if (project.getUsers().contains(allUser.get(i))) {
                System.out.println("for success, if success --- " + project.getUsers().size() + " - " + allUser.size());
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

        Helper.displayErrorMessage("Updated project successful", "The project \"" + project.getName() + "\" has been updated successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
    }

    private void onCancel() {
        this.close();
    }

}
