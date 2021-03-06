package com.copyrightException.ProjectManager.entities;

import com.copyrightException.ProjectManager.SlotColors;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Slot {

    private String id;
    private String name;
    private int position;
    private Project project;
    private List<Task> tasks;
    private SlotColors color = SlotColors.PRUSSIAN_BLUE;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    @Column(name = "columnName")
    public String getName() {
        return name;
    }

    @Column(name = "position")
    public int getPosition() {
        return position;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "slot")
    public List<Task> getTasks() {
        return tasks;
    }

    @ManyToOne()
    @JoinColumn(name = "projectId", nullable = false)
    public Project getProject() {
        return project;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public SlotColors getColor() {
        return color;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setColor(SlotColors color) {
        this.color = color;
    }

}
