package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.entities.Slot;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SlotComponent extends Panel {

    private final Button bChangeName = new Button();
    private final Button bRemove = new Button();
    private final Label laName = new Label();
    private final Button bAdd = new Button();
    private final SlotChangeListener slotChangedCallback;
    private final TaskComponent.TaskChangeListener taskChangeListener;
    private final VerticalLayout layout = new VerticalLayout();
    private final HorizontalLayout nameLayout = new HorizontalLayout();
    private final Slot slot;

    public SlotComponent(
            final Slot slot,
            final SlotChangeListener slotNameChangedCallback,
            final TaskComponent.TaskChangeListener taskChangeListener) {
        this.slotChangedCallback = slotNameChangedCallback;
        this.slot = slot;
        this.taskChangeListener = taskChangeListener;
        initLayout();
        initUi();
    }

    private void initLayout() {
        nameLayout.addComponent(laName);
        nameLayout.setExpandRatio(laName, 1);
        nameLayout.addComponent(bChangeName);
        nameLayout.addComponent(bRemove);
        nameLayout.setWidth("100%");
        layout.addComponent(nameLayout);
        slot.getTasks().forEach(task -> {
            layout.addComponent(new TaskComponent(task, taskChangeListener));
        });
        layout.addComponent(bAdd);
        layout.setComponentAlignment(bAdd, Alignment.MIDDLE_CENTER);
        layout.setWidth("100%");
        setContent(layout);
    }

    private void initUi() {
        bChangeName.setIcon(VaadinIcons.PENCIL);
        bChangeName.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bChangeName.addStyleName(ValoTheme.BUTTON_SMALL);
        bRemove.addClickListener(event -> onRemoveSlot());
        bRemove.setIcon(VaadinIcons.MINUS_CIRCLE);
        bRemove.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bRemove.addStyleName(ValoTheme.BUTTON_SMALL);
        bRemove.addStyleName(ValoTheme.BUTTON_DANGER);
        bAdd.setIcon(VaadinIcons.PLUS);
        bAdd.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bAdd.addClickListener(event -> onAddTask());
        laName.setValue(slot.getName());
        setWidth("300px");
        setHeight("100%");
    }

    private void onRemoveSlot() {
        slotChangedCallback.removeSlot(slot);
    }

    private void onSlotNameChanged(final String name) {
        slotChangedCallback.nameChanged(slot, name);
    }
    
    private void onAddTask(){
        slotChangedCallback.addNewTask(slot);
    }

    public static interface SlotChangeListener {

        public void removeSlot(final Slot slot);

        public void nameChanged(final Slot slot, final String name);

        public void addNewTask(final Slot slot);
    }
}
