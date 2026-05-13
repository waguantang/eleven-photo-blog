package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrackRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String artist;
    private String url;
    private String cover;
    private Integer duration;
    private Integer sortOrder;
}
