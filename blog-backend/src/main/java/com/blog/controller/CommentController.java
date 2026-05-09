package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.dto.CommentCreateRequest;
import com.blog.dto.CommentStatusRequest;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments")
    public Result<List<Comment>> getByPostId(@RequestParam Long postId) {
        return Result.success(commentService.getByPostId(postId));
    }

    @PostMapping("/comments")
    public Result<Map<String, Object>> create(@Valid @RequestBody CommentCreateRequest request) {
        Comment comment = commentService.create(request);
        Map<String, Object> data = new HashMap<>();
        data.put("id", comment.getId());
        data.put("message", "评论已提交，等待审核");
        return Result.success(data);
    }

    // ---- Admin ----

    @GetMapping("/admin/comments")
    public Result<PageResult<Comment>> listAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long postId) {
        return Result.success(commentService.listAdmin(page, size, status, postId));
    }

    @PutMapping("/admin/comments/{id}")
    public Result<Comment> updateStatus(@PathVariable Long id, @Valid @RequestBody CommentStatusRequest request) {
        return Result.success(commentService.updateStatus(id, request.getStatus()));
    }

    @DeleteMapping("/admin/comments/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success();
    }

    @GetMapping("/admin/comments/pending-count")
    public Result<Map<String, Long>> pendingCount() {
        Map<String, Long> data = new HashMap<>();
        data.put("count", commentService.countPending());
        return Result.success(data);
    }
}
