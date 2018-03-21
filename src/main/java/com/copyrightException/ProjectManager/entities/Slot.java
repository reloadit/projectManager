package com.copyrightException.ProjectManager.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Slot {

    private String id;
    private String name;
    private int position;
    private Project project;
    private List<Task> tasks;

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

    @OneToMany(mappedBy = "slot")
    public List<Task> getTasks() {
        return tasks;
    }

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    public Project getProject() {
        return project;
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

}
