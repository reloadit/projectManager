package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Slot;
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

public class SlotCreationWindow extends Window {

    private static final Logger LOG = LoggerFactory.getLogger(SlotCreationWindow.class);
    private final Button bCreateProject = new Button();
    private final Button bCancel = new Button();
    private final TextField tfSlotName = new TextField();
    private final Consumer<Slot> createSlotCallback;
    private final Binder<Slot> binder = new Binder();
    private final boolean create;
    private final Slot slot;

    public SlotCreationWindow(final Consumer<Slot> createSlotCallback) {
        this(createSlotCallback, true, new Slot());
    }

    public SlotCreationWindow(final Consumer<Slot> createSlotCallback, final boolean create, final Slot slot) {
        super(create
                ? "Create slot"
                : "Edit slot");
        this.slot = slot;
        this.create = create;
        this.createSlotCallback = createSlotCallback;
        initLayout();
        initUi();
        initBinder();
    }

    private void initLayout() {
        final FormLayout formLayout = new FormLayout(tfSlotName);
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
        tfSlotName.setCaption("Slot name");

        bCreateProject.setCaption(create
                ? "Create"
                : "Saves");
        bCreateProject.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bCreateProject.addClickListener(event -> onCreateProject());

        bCancel.setCaption("Cancel");
        bCancel.addClickListener(event -> onCancel());

        tfSlotName.addShortcutListener(new ShortcutListener("Create slot", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onCreateProject();
            }
        });
        tfSlotName.focus();

        this.setResizable(false);
        this.center();
        this.setModal(true);
        this.setVisible(true);
    }

    private void initBinder() {
        binder.forField(tfSlotName)
                .withValidator(new StringLengthValidator("Slot name must be between 1 and 255 characters long", 1, 255))
                .bind(Slot::getName, Slot::setName);
    }

    private void onCreateProject() {

        try {
            binder.writeBean(slot);
            createSlotCallback.accept(slot);
            this.close();
        } catch (ValidationException ex) {
            LOG.error("Validation exception");
        }
    }

    private void onCancel() {
        this.close();
    }
}
