package com.copyrightException.ProjectManager.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class User {

    private String id;
    private String name;
    private String passwortHash;
    private List<Project> projects;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name", unique=true)
    public String getName() {
        return name;
    }
    @Column(name = "password")
    public String getPasswortHash() {
        return passwortHash;
    }

    @ManyToMany(mappedBy = "users")
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
    

    public void setName(String name) {
        this.name = name;
    }


    public void setPasswortHash(String passwortHash) {
        this.passwortHash = passwortHash;
    }

}
