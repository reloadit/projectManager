package com.copyrightException.ProjectManager.repositories;

import com.copyrightException.ProjectManager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
