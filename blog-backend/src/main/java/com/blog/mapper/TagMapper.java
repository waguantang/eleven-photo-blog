package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    @Select("SELECT t.* FROM t_tag t INNER JOIN t_post_tag pt ON t.id = pt.tag_id WHERE pt.post_id = #{postId}")
    List<Tag> selectByPostId(Long postId);

    @Select("SELECT * FROM t_tag WHERE slug = #{slug}")
    Tag selectBySlug(String slug);
}
