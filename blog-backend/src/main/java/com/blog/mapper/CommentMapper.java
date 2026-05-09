package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT * FROM t_comment WHERE post_id = #{postId} AND parent_id IS NULL AND status = 'approved' ORDER BY created_at DESC")
    List<Comment> selectTopLevelByPostId(Long postId);

    @Select("SELECT * FROM t_comment WHERE parent_id = #{parentId} AND status = 'approved' ORDER BY created_at ASC")
    List<Comment> selectReplies(Long parentId);
}
