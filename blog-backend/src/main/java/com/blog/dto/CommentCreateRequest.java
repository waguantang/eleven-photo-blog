package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @NotNull(message = "文章ID不能为空")
    private Long postId;
    private Long parentId;
    @NotBlank(message = "昵称不能为空")
    private String authorName;
    private String authorEmail;
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
