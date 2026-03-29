package com.example.scss.repository;

import com.example.scss.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);
    List<Course> findByTeacherId(Long teacherId);
    List<Course> findByNameContaining(String name);
}
