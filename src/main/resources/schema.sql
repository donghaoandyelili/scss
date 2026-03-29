-- 创建数据库
CREATE DATABASE IF NOT EXISTS scss DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE scss;

-- 注意：表结构由JPA自动创建（ddl-auto: update）
-- 以下仅提供参考和手动建表时使用

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    role VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    create_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    credit INT NOT NULL,
    max_capacity INT NOT NULL,
    current_count INT NOT NULL DEFAULT 0,
    teacher_id BIGINT,
    schedule VARCHAR(100),
    classroom VARCHAR(100),
    description VARCHAR(500),
    create_time DATETIME,
    FOREIGN KEY (teacher_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 选课表
CREATE TABLE IF NOT EXISTS course_selection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SELECTED',
    select_time DATETIME,
    UNIQUE KEY uk_student_course (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES sys_user(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
