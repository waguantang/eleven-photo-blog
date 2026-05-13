package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PhotoRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String description;
    @NotBlank(message = "图片路径不能为空")
    private String src;
    private String category;
    private Integer sortOrder;
}
