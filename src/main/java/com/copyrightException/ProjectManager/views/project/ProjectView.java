package com.copyrightException.ProjectManager.views.project;

import com.copyrightException.ProjectManager.Helper;
import com.copyrightException.ProjectManager.entities.Project;
import com.copyrightException.ProjectManager.entities.Slot;
import com.copyrightException.ProjectManager.entities.Task;
import com.copyrightException.ProjectManager.entities.User;
import com.copyrightException.ProjectManager.repositories.ProjectRepository;
import com.copyrightException.ProjectManager.repositories.SlotRepository;
import com.copyrightException.ProjectManager.repositories.TaskRepository;
import com.copyrightException.ProjectManager.repositories.UserRepository;
import com.copyrightException.ProjectManager.views.project.components.Header;
import com.copyrightException.ProjectManager.views.project.components.SlotComponent;
import com.copyrightException.ProjectManager.views.project.components.SlotCreationWindow;
import com.copyrightException.ProjectManager.views.project.components.TaskWindow;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = ProjectView.VIEW_NAME)
public class ProjectView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectView.class);
    public static final String VIEW_NAME = "project";

    private final Label laProjectName = new Label();
    private final Panel paHeader = new Panel();
    private final Button bEditProject = new Button();
    private final HorizontalLayout layoutSlots = new HorizontalLayout();
    private final Button bAddSlot = new Button();
    private final Header header;
    private final Panel paSlot = new Panel();
    private final ProjectPresenter presenter;

    @Autowired
    public ProjectView(
            final ProjectRepository projectRepository,
            final SlotRepository slotRepository,
            final TaskRepository taskRepository,
            final UserRepository userRepository) {
        presenter = new ProjectPresenter(projectRepository, slotRepository, taskRepository, userRepository);
        bEditProject.setCaption("Edit Project");
        this.header = new Header(userRepository::saveAndFlush);
    }

    @PostConstruct
    public void init() {
        initLayout();
        initUi();
        presenter.setView(this);
        bEditProject.addClickListener(event -> onEdit());
    }

    private void initHeaderLayout() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSizeFull();
        layout.addComponent(laProjectName);
        layout.addComponent(bEditProject);
        layout.setComponentAlignment(bEditProject, Alignment.MIDDLE_RIGHT);
        paHeader.setContent(layout);

        laProjectName.setCaption("Project name:");
        laProjectName.addStyleName("v-text-bold-19px");
        bEditProject.addStyleName("v-text-17px");

        bEditProject.setIcon(VaadinIcons.TASKS);
    }

    private void initLayout() {
        initHeaderLayout();
        setSizeFull();
        setMargin(false);

        header.addStyleName("v-white55PercentBG");
        paHeader.addStyleName("v-white55PercentBG");

        addStyleName("v-image-transparent-projectView-BG");
        
        paSlot.addStyleName("v-invisible-panel");
        paSlot.setContent(layoutSlots);
        paSlot.setSizeFull();
        
        addComponent(header);
        addComponent(paHeader);
        addComponent(paSlot);
        setExpandRatio(paSlot, 1);
        layoutSlots.setHeight("100%");
    }

    private void initUi() {
        bAddSlot.setIcon(VaadinIcons.PLUS);
        bAddSlot.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        bAddSlot.addStyleName("v-white55PercentBG-slightlyRoundCorners");
        bAddSlot.addClickListener(event -> addSlot());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        View.super.enter(event);
        final String projectId = event.getParameterMap().get("project");
        LOG.info(String.format("enter() project parameters: %s", projectId));
        presenter.onEnter(projectId);
        header.viewEnter();
    }

    private void onEdit() {
        presenter.onEditProject();
    }

    public void projectNotFound() {
        final Notification notification = new Notification("Project not found",
                "The project couldn't be found",
                Notification.Type.WARNING_MESSAGE);
        notification.setDelayMsec(1500);
        notification.setPosition(Position.TOP_CENTER);
        notification.show(Page.getCurrent());
        UI.getCurrent().getNavigator().navigateTo(ProjectOverview.VIEW_NAME);
    }

    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {
        View.super.beforeLeave(event);
        presenter.beforeLeave();
        header.viewLeave();
    }

    public void setProject(final Project project) {
        laProjectName.setValue(project.getName());
        layoutSlots.removeAllComponents();
        bEditProject.setEnabled(Helper.getUser().equals(project.getCreator()));
        project.getSlots().forEach(slot -> {
            final SlotComponent component = new SlotComponent(slot, presenter, presenter);

            final DragSourceExtension<SlotComponent> dragSource = new DragSourceExtension<>(component);
            dragSource.setDragData(slot);
            dragSource.setEffectAllowed(EffectAllowed.MOVE);
            layoutSlots.addComponent(component);

            final DropTargetExtension<SlotComponent> dropTarget = new DropTargetExtension<>(component);
            dropTarget.setDropEffect(DropEffect.MOVE);
            dropTarget.addDropListener(event -> {
                LOG.info(String.format("Drop event, drag data: %s", event.getDragData().orElse(null)));
                event.getDragData()
                        .ifPresent(obj -> handleDragData(obj, slot));
            });
        });
        layoutSlots.addComponent(bAddSlot);
        layoutSlots.setComponentAlignment(bAddSlot, Alignment.MIDDLE_CENTER);
    }

    private void handleDragData(final Object dragData, final Slot slot) {
        if (dragData instanceof Task) {
            presenter.moveTask((Task) dragData, slot);
        } else if (dragData instanceof Slot) {
            presenter.swapSlot((Slot) dragData, slot);
        } else {
            LOG.error(String.format("Unknown dragData: %s", dragData));
        }
    }

    private void addSlot() {
        final SlotCreationWindow window = new SlotCreationWindow(slot -> presenter.addSlot(slot));
        window.center();
        window.setModal(true);
        window.setVisible(true);
        UI.getCurrent().addWindow(window);
    }

    public void showAddTaskDialog(
            final boolean create,
            final Task task,
            final List<User> users,
            final Consumer<Task> callback) {
        final TaskWindow window = new TaskWindow(create, task, callback, t -> presenter.deleteTask(t), users);
        window.center();
        window.setModal(true);
        window.setVisible(true);
        UI.getCurrent().addWindow(window);
    }
}
