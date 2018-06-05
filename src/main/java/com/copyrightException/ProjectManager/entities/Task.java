package com.copyrightException.ProjectManager.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Task {

    private String id;
    private String name;
    private String description;
    private int position;
    private User assignedUser;
    private Slot slot;
    private Boolean done;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    @Column(name = "position")
    public int getPosition() {
        return position;
    }

    @Column(name = "taskName")
    public String getName() {
        return name;
    }

    @Column(name = "description", length = 1024)
    public String getDescription() {
        return description;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = true)
    public User getAssignedUser() {
        return assignedUser;
    }

    @ManyToOne()
    @JoinColumn(name = "slotId", nullable = false)
    public Slot getSlot() {
        return slot;
    }

    @Column(name = "taskDone")
    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
