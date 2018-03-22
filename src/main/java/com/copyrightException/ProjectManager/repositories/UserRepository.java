package com.copyrightException.ProjectManager.repositories;

import com.copyrightException.ProjectManager.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByName(String name);
}
