package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_track")
public class Track {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String artist;
    private String url;
    private String cover;
    private Integer duration;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
