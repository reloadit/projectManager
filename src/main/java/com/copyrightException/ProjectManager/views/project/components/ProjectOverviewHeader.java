package com.copyrightException.ProjectManager.views.project.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ProjectOverviewHeader extends Panel {

    private final Button bCreateProject = new Button();

    private Runnable createProjectCallback = null;

    public ProjectOverviewHeader() {
        super();
        initLayout();
        initUi();
    }

    private void initLayout() {
        final VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        layout.addComponent(bCreateProject);
    }

    private void initUi() {
        bCreateProject.addStyleName("v-text-bold-17px");
        bCreateProject.setCaption("Create new project");
        bCreateProject.setIcon(VaadinIcons.PLUS_CIRCLE);
        bCreateProject.addClickListener(event -> onCreateProject());
    }

    public void setCreateProjectCallback(Runnable createProjectCallback) {
        this.createProjectCallback = createProjectCallback;
    }

    private void onCreateProject() {
        if (createProjectCallback != null) {
            createProjectCallback.run();
        }
    }
}
