package com.blog.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface PostTagMapper {

    @Insert("INSERT INTO t_post_tag (post_id, tag_id) VALUES (#{postId}, #{tagId})")
    void insert(@Param("postId") Long postId, @Param("tagId") Long tagId);

    @Delete("DELETE FROM t_post_tag WHERE post_id = #{postId}")
    void deleteByPostId(@Param("postId") Long postId);
}
