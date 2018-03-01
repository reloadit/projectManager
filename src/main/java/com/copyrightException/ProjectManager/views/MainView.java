package com.copyrightException.ProjectManager.views;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringView(name = MainView.viewName)
public class MainView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(MainView.class);
    public static final String viewName = "";

    private Button bClickMe;

    public MainView() {
        createComponents();
        initLayout();
        initUi();
    }

    private void createComponents() {
        bClickMe = new Button("click me");
    }

    private void initLayout() {
        addComponent(bClickMe);
    }

    private void initUi() {
        bClickMe.addClickListener(e -> onClickMeClick());
    }

    private void onClickMeClick() {
        LOG.info("click");
    }
}
