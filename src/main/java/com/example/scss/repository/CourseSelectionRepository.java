package com.example.scss.repository;

import com.example.scss.entity.CourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CourseSelectionRepository extends JpaRepository<CourseSelection, Long> {
    List<CourseSelection> findByStudentIdAndStatus(Long studentId, CourseSelection.Status status);
    List<CourseSelection> findByCourseIdAndStatus(Long courseId, CourseSelection.Status status);
    Optional<CourseSelection> findByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, CourseSelection.Status status);
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, CourseSelection.Status status);
    long countByCourseIdAndStatus(Long courseId, CourseSelection.Status status);
}
