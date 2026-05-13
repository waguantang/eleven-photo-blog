package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_photo")
public class Photo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String src;
    private String category;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
