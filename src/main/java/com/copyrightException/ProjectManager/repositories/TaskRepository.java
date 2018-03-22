package com.copyrightException.ProjectManager.repositories;

import com.copyrightException.ProjectManager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {

}
