package com.example.scss.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseRequest {
    @NotBlank(message = "课程名称不能为空")
    private String name;

    @NotBlank(message = "课程编号不能为空")
    private String code;

    @NotNull(message = "学分不能为空")
    @Min(value = 1, message = "学分至少为1")
    private Integer credit;

    @NotNull(message = "最大容量不能为空")
    @Min(value = 1, message = "最大容量至少为1")
    private Integer maxCapacity;

    private Long teacherId;
    private String schedule;
    private String classroom;
    private String description;
}
