package com.copyrightException.ProjectManager.views.project.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ConfirmWindow extends Window {

    private final Button bYes = new Button();
    private final Button bNo = new Button();
    private final Label laText = new Label();
    private final Runnable confirmCallback;
    private final Runnable noCallback;

    public ConfirmWindow(Runnable confirmCallback, final String title, final String text) {
        this(confirmCallback, () -> {
        }, title, text);
    }

    public ConfirmWindow(Runnable confirmCallback, Runnable noCallback, final String title, final String text) {
        this.confirmCallback = confirmCallback;
        this.noCallback = noCallback;
        this.setCaption(title);
        laText.setValue(text);
        initLayout();
        initUi();
    }

    private void initUi() {
        bNo.addClickListener(e -> onNo());
        bNo.setCaption("No");
        bYes.addClickListener(e -> onYes());
        bYes.setCaption("Yes");
        this.center();
        this.setModal(true);
        this.setVisible(true);
    }

    private void initLayout() {
        final HorizontalLayout buttonLayout = new HorizontalLayout(bYes, bNo);
        final VerticalLayout layout = new VerticalLayout(laText, buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
        setContent(layout);
    }

    private void onNo() {
        noCallback.run();
        this.close();
    }

    private void onYes() {
        confirmCallback.run();
        this.close();
    }

}
