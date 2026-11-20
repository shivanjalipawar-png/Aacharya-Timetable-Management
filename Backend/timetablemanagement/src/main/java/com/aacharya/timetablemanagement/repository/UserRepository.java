package com.aacharya.timetablemanagement.repository;


import com.aacharya.timetablemanagement.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    @EntityGraph(attributePaths = {"teacher", "batch"})
    Optional<User> findWithTeacherAndBatchByUsername(String username);


}
