package com.example.scss.service;

import com.example.scss.dto.CourseRequest;
import com.example.scss.entity.Course;
import com.example.scss.entity.User;
import com.example.scss.repository.CourseRepository;
import com.example.scss.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
    }

    public List<Course> searchCourses(String name) {
        return courseRepository.findByNameContaining(name);
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    @Transactional
    public Course createCourse(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("课程编号已存在");
        }

        Course course = new Course();
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setCredit(request.getCredit());
        course.setMaxCapacity(request.getMaxCapacity());
        course.setSchedule(request.getSchedule());
        course.setClassroom(request.getClassroom());
        course.setDescription(request.getDescription());

        if (request.getTeacherId() != null) {
            User teacher = userRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("教师不存在"));
            if (teacher.getRole() != User.Role.TEACHER) {
                throw new RuntimeException("指定用户不是教师");
            }
            course.setTeacher(teacher);
        }

        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        course.setName(request.getName());
        course.setCredit(request.getCredit());
        course.setMaxCapacity(request.getMaxCapacity());
        course.setSchedule(request.getSchedule());
        course.setClassroom(request.getClassroom());
        course.setDescription(request.getDescription());

        if (request.getTeacherId() != null) {
            User teacher = userRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("教师不存在"));
            if (teacher.getRole() != User.Role.TEACHER) {
                throw new RuntimeException("指定用户不是教师");
            }
            course.setTeacher(teacher);
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("课程不存在");
        }
        courseRepository.deleteById(id);
    }
}
