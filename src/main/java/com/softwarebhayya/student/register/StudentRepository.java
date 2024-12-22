package com.softwarebhayya.student.register;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends CrudRepository<Student, String> {
    
    List<Student> findByName(String name);
    
}
