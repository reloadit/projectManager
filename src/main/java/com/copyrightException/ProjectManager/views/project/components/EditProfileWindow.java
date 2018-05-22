package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.User;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
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
    private TextField tfUserName;
    private TextField tfFirstname;
    private TextField tfLastname;
    private CheckBox changePwCheckbox;
    private PasswordField pfUserPasswordOld;
    private PasswordField pfUserPasswordNew;
    private PasswordField pfUserPasswordNewRepeat;
    private final Consumer<User> saveUserProfileCallBack;
    private final Binder<User> binder = new Binder();

    public EditProfileWindow(final Consumer<User> saveUserProfileCallBack) {
        super("Edit User Profile");
        this.saveUserProfileCallBack = saveUserProfileCallBack;
        initUi();
        initLayout();
    }

    private void initLayout() {
        final FormLayout formLayout = new FormLayout(tfUserName, tfFirstname, tfLastname, changePwCheckbox, pfUserPasswordOld, pfUserPasswordNew, pfUserPasswordNewRepeat);
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
        changePwCheckbox = new CheckBox();
        tfUserName = new TextField();
        tfFirstname = new TextField();
        tfLastname = new TextField();
        pfUserPasswordOld = new PasswordField();
        pfUserPasswordNew = new PasswordField();
        pfUserPasswordNewRepeat = new PasswordField();

        changePwCheckbox.setCaption("Change Password");
        tfUserName.setCaption("User name:");
        tfFirstname.setCaption("First name:");
        tfLastname.setCaption("Last name:");
        pfUserPasswordOld.setCaption("Old password:");
        pfUserPasswordNew.setCaption("New password:");
        pfUserPasswordNewRepeat.setCaption("Repeat new password:");

        bSave.setCaption("Save");
        bSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bSave.addClickListener(event -> onSaveProfile());

        bCancel.setCaption("Cancel");
        bCancel.addClickListener(event -> onCancel());

        User u = Helper.getUser();

        if (u.getName() != null) {
            tfUserName.setValue(u.getName());
        }
        if (u.getFirstName() != null) {
            tfFirstname.setValue(u.getFirstName());
        }
        if (u.getLastName() != null) {
            tfLastname.setValue(u.getLastName());
        }

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
        pfUserPasswordOld.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });
        pfUserPasswordNew.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });
        pfUserPasswordNewRepeat.addShortcutListener(new ShortcutListener("onEnter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                onSaveProfile();
            }
        });

        this.setResizable(false);
    }

    private void onSaveProfile() {

        if (tfUserName.isEmpty()) {
            Helper.displayErrorMessage("Empty Username", "Please enter a username", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (tfFirstname.isEmpty()) {
            Helper.displayErrorMessage("Empty First name", "Please enter a first name", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (tfLastname.isEmpty()) {
            Helper.displayErrorMessage("Empty Last name", "Please enter a last name", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (pfUserPasswordOld.isEmpty()) {
            Helper.displayErrorMessage("Empty Password", "Please enter your old password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        /*
        if (pfPassword.getValue().length() < 10) {
            Helper.displayErrorMessage("Password Length Insufficient", "Please enter a password with at least 10 characters", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (pfRepeatPassword.isEmpty()) {
            Helper.displayErrorMessage("Empty Password", "Please repeat your password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (!pfPassword.getValue().equals(pfRepeatPassword.getValue())) {
            Helper.displayErrorMessage("Passwords Unequal", "The passwords don`t match", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }

        if (!userRepository.findByName(tfUserName.getValue()).isEmpty()) {
            Helper.displayErrorMessage("Username Unavailable", "This username already exists", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
            return;
        }
        */

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
