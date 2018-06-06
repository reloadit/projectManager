package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.ProjecManagerEventBus;
import com.copyrightException.ProjectManager.entities.User;
import com.vaadin.data.HasValue;
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
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
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
    private User u;

    public EditProfileWindow(final Consumer<User> saveUserProfileCallBack) {
        super("Edit User Profile");
        this.saveUserProfileCallBack = saveUserProfileCallBack;
        initUi();
        initLayout();
        togglePasswordFields();
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

        u = Helper.getUser();

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

        changePwCheckbox.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                togglePasswordFields();
            }
        });

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

    private void togglePasswordFields() {
        pfUserPasswordOld.setEnabled(changePwCheckbox.getValue());
        pfUserPasswordNew.setEnabled(changePwCheckbox.getValue());
        pfUserPasswordNewRepeat.setEnabled(changePwCheckbox.getValue());
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

        if (changePwCheckbox.getValue()) {

            //check if old pw is correct
            SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
            byte[] enteredHashedPW = digestSHA3.digest(pfUserPasswordOld.getValue().getBytes());
            if (!Hex.toHexString(enteredHashedPW).equals(u.getPasswortHash())) {
                Helper.displayErrorMessage("Wrong Password", "Please enter the correct current password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
                return;
            }

            if (pfUserPasswordNew.getValue().length() < 10) {
                Helper.displayErrorMessage("New Password Length Insufficient", "Please enter a password with at least 10 characters", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
                return;
            }

            if (pfUserPasswordNewRepeat.isEmpty()) {
                Helper.displayErrorMessage("Empty Password", "Please repeat your password", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
                return;
            }

            if (!pfUserPasswordNew.getValue().equals(pfUserPasswordNewRepeat.getValue())) {
                Helper.displayErrorMessage("Passwords Unequal", "The passwords don`t match", Notification.Type.WARNING_MESSAGE, Position.TOP_CENTER, Page.getCurrent());
                return;
            }
        }

        //TODO > update user instead of creating new one
        u.setName(tfUserName.getValue());
        u.setFirstName(tfFirstname.getValue());
        u.setLastName(tfLastname.getValue());

        if (changePwCheckbox.getValue()) {
            SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
            byte[] hashedPW = digestSHA3.digest(pfUserPasswordNew.getValue().getBytes());
            u.setPasswortHash(Hex.toHexString(hashedPW));
        }

        saveUserProfileCallBack.accept(u);
        ProjecManagerEventBus.EVENT_BUS.post(u);

        this.close();

        Helper.displayErrorMessage("Updated user successful", "The user \"" + u.getName() + "\" has been updated successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
    }

    private void onCancel() {
        this.close();
    }

}
