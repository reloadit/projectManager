package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Project;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectCreationWindow extends Window {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectCreationWindow.class);
    private final Button bCreateProject = new Button();
    private final Button bCancel = new Button();
    private final TextField tfProjectName = new TextField();
    private final Consumer<Project> createProjectCallBack;
    private final Binder<Project> binder = new Binder();

    public ProjectCreationWindow(final Consumer<Project> createProjectCallBack) {
        super("Create project");
        this.createProjectCallBack = createProjectCallBack;
        initLayout();
        initUi();
        initBinder();
    }

    private void initLayout() {
        final FormLayout formLayout = new FormLayout(tfProjectName);
        final HorizontalLayout buttonLayout = new HorizontalLayout(bCreateProject, bCancel);
        final VerticalLayout layout = new VerticalLayout(formLayout, buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
        layout.setWidthUndefined();
        buttonLayout.setWidthUndefined();
        formLayout.setWidthUndefined();
        this.setWidthUndefined();
        this.setContent(layout);
    }

    private void initUi() {
        tfProjectName.setCaption("Project name");

        bCreateProject.setCaption("Create");
        bCreateProject.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bCreateProject.addClickListener(event -> onCreateProject());

        bCancel.setCaption("Cancel");
        bCancel.addClickListener(event -> onCancel());

        tfProjectName.addShortcutListener(new ShortcutListener("Create project", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onCreateProject();
            }
        });
        tfProjectName.focus();

        this.setResizable(false);
    }

    private void initBinder() {
        binder.forField(tfProjectName)
                .withValidator(new StringLengthValidator("Project name must be between 1 and 255 characters long", 1, 255))
                .bind(Project::getName, Project::setName);
    }

    private void onCreateProject() {
        final Project project = new Project();
        try {
            binder.writeBean(project);
            createProjectCallBack.accept(project);
            this.close();
        } catch (ValidationException ex) {
            LOG.error("Validation exception");
        }
    }

    private void onCancel() {
        this.close();
    }
}
