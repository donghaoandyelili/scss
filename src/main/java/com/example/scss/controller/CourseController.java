package com.example.scss.controller;

import com.example.scss.dto.CourseRequest;
import com.example.scss.dto.Result;
import com.example.scss.entity.Course;
import com.example.scss.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Result<List<Course>> getAllCourses() {
        return Result.success(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public Result<Course> getCourse(@PathVariable Long id) {
        return Result.success(courseService.getCourseById(id));
    }

    @GetMapping("/search")
    public Result<List<Course>> searchCourses(@RequestParam String name) {
        return Result.success(courseService.searchCourses(name));
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        return Result.success(courseService.getCoursesByTeacher(teacherId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Course> createCourse(@Valid @RequestBody CourseRequest request) {
        return Result.success("课程创建成功", courseService.createCourse(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return Result.success("课程更新成功", courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success("课程删除成功", null);
    }
}
