package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String coverImage;
    private String status;   // draft / published
    private Long categoryId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    @TableField(exist = false)
    private Category category;
    @TableField(exist = false)
    private java.util.List<Tag> tags;
}
