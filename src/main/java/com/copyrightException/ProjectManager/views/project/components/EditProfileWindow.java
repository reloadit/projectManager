package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.User;
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

public class EditProfileWindow extends Window {

    private static final Logger LOG = LoggerFactory.getLogger(EditProfileWindow.class);
    private final Button bSave = new Button();
    private final Button bCancel = new Button();
    private final TextField tfUserName = new TextField();
    private final TextField tfFirstname = new TextField();
    private final TextField tfLastname = new TextField();
    private final TextField tfUserPasswordOld = new TextField();
    private final TextField tfUserPasswordNew = new TextField();
    private final TextField tfUserPasswordNewRepeat = new TextField();
    private final Consumer<User> saveUserProfileCallBack;
    private final Binder<User> binder = new Binder();

    public EditProfileWindow(final Consumer<User> saveUserProfileCallBack) {
        super("Edit User Profile");
        this.saveUserProfileCallBack = saveUserProfileCallBack;
        initLayout();
        initUi();
        initBinder();
    }

    private void initLayout() {
        final FormLayout formLayout = new FormLayout(tfUserName, tfFirstname, tfLastname, tfUserPasswordOld, tfUserPasswordNew, tfUserPasswordNewRepeat);
        final HorizontalLayout buttonLayout = new HorizontalLayout(bSave, bCancel);
        final VerticalLayout layout = new VerticalLayout(formLayout, buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
        layout.setWidthUndefined();
        buttonLayout.setWidthUndefined();
        formLayout.setWidthUndefined();
        this.setWidthUndefined();
        this.setContent(layout);
    }

    private void initUi() {
        tfUserName.setCaption("User name");
        tfFirstname.setCaption("First name");
        tfLastname.setCaption("Last name");
        tfUserPasswordOld.setCaption("Old name");
        tfUserPasswordNew.setCaption("New name");
        tfUserPasswordNewRepeat.setCaption("Repeat new password");

        bSave.setCaption("Save");
        bSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bSave.addClickListener(event -> onSaveProfile());

        bCancel.setCaption("Cancel");
        bCancel.addClickListener(event -> onCancel());

        tfUserName.focus();

        tfUserName.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });
        tfFirstname.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });
        tfLastname.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });
        tfUserPasswordOld.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });
        tfUserPasswordNew.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });
        tfUserPasswordNewRepeat.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });

        this.setResizable(false);
    }

    private void initBinder() {
        binder.forField(tfUserName)
                .withValidator(new StringLengthValidator("Project name must be between 1 and 16 characters long", 1, 16))
                .bind(User::getName, User::setName);
        binder.forField(tfFirstname)
                .bind(User::getFirstName, User::setFirstName);
        binder.forField(tfLastname)
                .bind(User::getLastName, User::setLastName);
        //TODO bind password fieds
    }

    private void onSaveProfile() {
        final User user = new User();
        try {
            binder.writeBean(user);
            saveUserProfileCallBack.accept(user);
            this.close();
        } catch (ValidationException ex) {
            LOG.error("Validation exception");
        }
    }

    private void onCancel() {
        this.close();
    }

}
