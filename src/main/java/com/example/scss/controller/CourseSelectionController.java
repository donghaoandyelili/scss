package com.example.scss.controller;

import com.example.scss.dto.Result;
import com.example.scss.entity.CourseSelection;
import com.example.scss.entity.User;
import com.example.scss.repository.UserRepository;
import com.example.scss.service.CourseSelectionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/selections")
public class CourseSelectionController {

    private final CourseSelectionService selectionService;
    private final UserRepository userRepository;

    public CourseSelectionController(CourseSelectionService selectionService, UserRepository userRepository) {
        this.selectionService = selectionService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<CourseSelection> selectCourse(@PathVariable Long courseId, Authentication auth) {
        User student = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return Result.success("选课成功", selectionService.selectCourse(student.getId(), courseId));
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Void> dropCourse(@PathVariable Long courseId, Authentication auth) {
        User student = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        selectionService.dropCourse(student.getId(), courseId);
        return Result.success("退课成功", null);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<List<CourseSelection>> mySelections(Authentication auth) {
        User student = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return Result.success(selectionService.getStudentSelections(student.getId()));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public Result<List<CourseSelection>> getCourseStudents(@PathVariable Long courseId) {
        return Result.success(selectionService.getCourseStudents(courseId));
    }
}
