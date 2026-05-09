package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentStatusRequest {
    @NotBlank(message = "状态不能为空")
    private String status;  // approved / rejected
}
