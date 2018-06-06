package com.copyrightException.ProjectManager.entities;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class User {

    private String id;
    private String name;
    private String firstName;
    private String lastName;
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

    @Column(name = "name", unique = true)
    public String getName() {
        return name;
    }

    @Column(name = "password")
    public String getPasswortHash() {
        return passwortHash;
    }

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    public List<Project> getProjects() {
        return projects;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public String abbrvname() {
        try {
            String firstChar = String.valueOf(firstName.charAt(0)).toUpperCase();
            String lastChar = String.valueOf(lastName.charAt(0)).toUpperCase();
            return firstChar + lastChar;
        } catch (Exception e) {
            return "";
        }
    }

}
