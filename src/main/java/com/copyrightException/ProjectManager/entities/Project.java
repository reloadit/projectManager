package com.copyrightException.ProjectManager.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Project {

    private String id;
    private String name;
    private User creator;
    private List<User> users;
    private List<Slot> slots;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    @Column(name = "projectName")
    public String getName() {
        return name;
    }
    
    @OneToMany(mappedBy = "project", fetch=FetchType.EAGER)
    public List<Slot> getSlots() {
        return slots;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = true)
    public User getCreator() {
        return creator;
    }

    @ManyToMany(cascade = {CascadeType.DETACH}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "Project_User",
            joinColumns = {
                @JoinColumn(name = "projectId")},
            inverseJoinColumns = {
                @JoinColumn(name = "userId")}
    )
    public List<User> getUsers() {
        return users;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
