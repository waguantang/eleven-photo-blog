package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    @Select("SELECT * FROM t_post WHERE slug = #{slug}")
    Post selectBySlug(String slug);

    @Select("SELECT * FROM t_post WHERE id < #{id} AND status = 'published' ORDER BY id DESC LIMIT 1")
    Post selectPrev(Long id);

    @Select("SELECT * FROM t_post WHERE id > #{id} AND status = 'published' ORDER BY id ASC LIMIT 1")
    Post selectNext(Long id);

    @Select("SELECT DISTINCT DATE_FORMAT(published_at, '%Y-%m') AS month FROM t_post WHERE status = 'published' ORDER BY month DESC")
    List<String> selectArchiveMonths();
}
