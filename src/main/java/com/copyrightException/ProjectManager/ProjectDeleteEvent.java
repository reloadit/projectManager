package com.copyrightException.ProjectManager;

public class ProjectDeleteEvent {

    private final String id;

    public ProjectDeleteEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
