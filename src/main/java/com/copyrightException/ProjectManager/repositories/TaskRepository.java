package com.copyrightException.ProjectManager.repositories;

import com.copyrightException.ProjectManager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

}
