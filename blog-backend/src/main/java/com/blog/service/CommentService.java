package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.BizException;
import com.blog.common.PageResult;
import com.blog.dto.CommentCreateRequest;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> getByPostId(Long postId) {
        List<Comment> topLevel = commentMapper.selectTopLevelByPostId(postId);
        for (Comment c : topLevel) {
            c.setReplies(commentMapper.selectReplies(c.getId()));
        }
        return topLevel;
    }

    public Comment create(CommentCreateRequest req) {
        if (req.getParentId() != null && !commentMapper.exists(
                new LambdaQueryWrapper<Comment>().eq(Comment::getId, req.getParentId()))) {
            throw new BizException(404, "父评论不存在");
        }
        Comment comment = new Comment();
        comment.setPostId(req.getPostId());
        comment.setParentId(req.getParentId());
        comment.setAuthorName(req.getAuthorName());
        comment.setAuthorEmail(req.getAuthorEmail());
        comment.setContent(req.getContent());
        comment.setStatus("pending");
        comment.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(comment);
        return comment;
    }

    public PageResult<Comment> listAdmin(int page, int size, String status, Long postId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<Comment>()
                .orderByDesc(Comment::getCreatedAt);
        if (status != null && !status.isEmpty()) wrapper.eq(Comment::getStatus, status);
        if (postId != null) wrapper.eq(Comment::getPostId, postId);

        Page<Comment> pg = commentMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(pg.getRecords(), pg.getTotal(), page, size);
    }

    public Comment updateStatus(Long id, String status) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) throw new BizException(404, "评论不存在");
        comment.setStatus(status);
        commentMapper.updateById(comment);
        return comment;
    }

    public void delete(Long id) {
        commentMapper.deleteById(id);
    }

    public long countPending() {
        return commentMapper.selectCount(
                new LambdaQueryWrapper<Comment>().eq(Comment::getStatus, "pending"));
    }
}
