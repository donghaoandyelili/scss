package com.example.scss.service;

import com.example.scss.entity.Course;
import com.example.scss.entity.CourseSelection;
import com.example.scss.entity.User;
import com.example.scss.repository.CourseRepository;
import com.example.scss.repository.CourseSelectionRepository;
import com.example.scss.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseSelectionService {

    private final CourseSelectionRepository selectionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseSelectionService(CourseSelectionRepository selectionRepository,
                                  CourseRepository courseRepository,
                                  UserRepository userRepository) {
        this.selectionRepository = selectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CourseSelection selectCourse(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        if (student.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("只有学生才能选课");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        if (selectionRepository.existsByStudentIdAndCourseIdAndStatus(
                studentId, courseId, CourseSelection.Status.SELECTED)) {
            throw new RuntimeException("已经选过该课程");
        }

        long selectedCount = selectionRepository.countByCourseIdAndStatus(
                courseId, CourseSelection.Status.SELECTED);
        if (selectedCount >= course.getMaxCapacity()) {
            throw new RuntimeException("课程已满，无法选课");
        }

        CourseSelection selection = new CourseSelection();
        selection.setStudent(student);
        selection.setCourse(course);
        selection.setStatus(CourseSelection.Status.SELECTED);

        course.setCurrentCount((int) selectedCount + 1);
        courseRepository.save(course);

        return selectionRepository.save(selection);
    }

    @Transactional
    public void dropCourse(Long studentId, Long courseId) {
        CourseSelection selection = selectionRepository
                .findByStudentIdAndCourseIdAndStatus(studentId, courseId, CourseSelection.Status.SELECTED)
                .orElseThrow(() -> new RuntimeException("未选该课程"));

        selection.setStatus(CourseSelection.Status.DROPPED);
        selectionRepository.save(selection);

        Course course = selection.getCourse();
        long selectedCount = selectionRepository.countByCourseIdAndStatus(
                courseId, CourseSelection.Status.SELECTED);
        course.setCurrentCount((int) selectedCount);
        courseRepository.save(course);
    }

    public List<CourseSelection> getStudentSelections(Long studentId) {
        return selectionRepository.findByStudentIdAndStatus(studentId, CourseSelection.Status.SELECTED);
    }

    public List<CourseSelection> getCourseStudents(Long courseId) {
        return selectionRepository.findByCourseIdAndStatus(courseId, CourseSelection.Status.SELECTED);
    }
}
