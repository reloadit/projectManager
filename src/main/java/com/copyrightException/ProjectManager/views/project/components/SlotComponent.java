package com.copyrightException.ProjectManager.views.project.components;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Slot;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.DragSourceExtension;
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
        if(slot.getColor() != null){
            this.addStyleName(slot.getColor().getStyleName());
        }
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
            final TaskComponent taskComponent = new TaskComponent(task, taskChangeListener);

            taskComponent.addClickListener(new MouseEvents.ClickListener() {
                @Override
                public void click(MouseEvents.ClickEvent event) {
                    taskComponent.editTask();
                }
            });

            DragSourceExtension<TaskComponent> dragSourceExtension = new DragSourceExtension<>(taskComponent);
            dragSourceExtension.setEffectAllowed(EffectAllowed.MOVE);
            dragSourceExtension.setDragData(task);
            layout.addComponent(taskComponent);
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
        bChangeName.addStyleName("pm-slot-button");
        bChangeName.addClickListener(e -> onEditSlot());
        bRemove.addClickListener(event -> onRemoveSlot());
        bRemove.setIcon(VaadinIcons.MINUS_CIRCLE);
        bRemove.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bRemove.addStyleName(ValoTheme.BUTTON_SMALL);
        bRemove.addStyleName(ValoTheme.BUTTON_DANGER);
        bRemove.addStyleName("pm-slot-button");
        bAdd.setIcon(VaadinIcons.PLUS);
        bAdd.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        bAdd.addStyleName("pm-slot-button");
        bAdd.addClickListener(event -> onAddTask());
        laName.setValue(slot.getName());
        laName.addStyleName("v-text-bold-19px");
        laName.addStyleName("pm-slot-text");
        setWidth("300px");
        setHeight("100%");
    }

    private void onEditSlot() {
        final SlotCreationWindow slotEditWindow = new SlotCreationWindow(slotChangedCallback::slotChanged, false, slot);
        UI.getCurrent().addWindow(slotEditWindow);
    }

    private void onRemoveSlot() {
        final ConfirmWindow confirmWindow = new ConfirmWindow(() -> {
            Helper.displayErrorMessage("Deleted slot successfully", "The slot \"" + slot.getName() + "\" has been deleted successfully", Notification.Type.ASSISTIVE_NOTIFICATION, Position.TOP_CENTER, Page.getCurrent());
            slotChangedCallback.removeSlot(slot);
        },
                "Delete Slot",
                String.format("Are you sure that you want to delete the slot: %s", slot.getName()));
        UI.getCurrent().addWindow(confirmWindow);
    }

    private void onAddTask() {
        slotChangedCallback.addNewTask(slot);
    }

    public static interface SlotChangeListener {

        public void removeSlot(final Slot slot);

        public void slotChanged(final Slot slot);

        public void addNewTask(final Slot slot);
    }
}
