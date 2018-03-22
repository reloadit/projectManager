package com.copyrightException.ProjectManager.repositories;

import com.copyrightException.ProjectManager.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {

}
