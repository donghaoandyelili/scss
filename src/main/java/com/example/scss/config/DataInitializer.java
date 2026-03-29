package com.example.scss.config;

import com.example.scss.entity.User;
import com.example.scss.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRealName("系统管理员");
            admin.setRole(User.Role.ADMIN);
            admin.setEmail("admin@example.com");
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("teacher1")) {
            User teacher = new User();
            teacher.setUsername("teacher1");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setRealName("张老师");
            teacher.setRole(User.Role.TEACHER);
            teacher.setEmail("teacher1@example.com");
            userRepository.save(teacher);
        }

        if (!userRepository.existsByUsername("student1")) {
            User student = new User();
            student.setUsername("student1");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRealName("李同学");
            student.setRole(User.Role.STUDENT);
            student.setEmail("student1@example.com");
            userRepository.save(student);
        }
    }
}
