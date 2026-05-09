package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class PostCreateRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    private String excerpt;
    private String coverImage;
    private String status = "draft";
    private Long categoryId;
    private List<Long> tagIds;
}
