package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.dto.PostCreateRequest;
import com.blog.dto.PostUpdateRequest;
import com.blog.entity.Post;
import com.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // ---- Public ----

    @GetMapping("/posts")
    public Result<PageResult<Post>> listPublic(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long tag,
            @RequestParam(required = false) String keyword) {
        return Result.success(postService.listPublic(page, size, category, tag, keyword));
    }

    @GetMapping("/posts/{slug}")
    public Result<Post> getBySlug(@PathVariable String slug) {
        return Result.success(postService.getBySlug(slug));
    }

    @GetMapping("/posts/{id}/prev-next")
    public Result<Map<String, Post>> prevNext(@PathVariable Long id) {
        Map<String, Post> map = new HashMap<>();
        map.put("prev", postService.getPrev(id));
        map.put("next", postService.getNext(id));
        return Result.success(map);
    }

    @GetMapping("/archive-months")
    public Result<List<String>> archiveMonths() {
        return Result.success(postService.getArchiveMonths());
    }

    // ---- Admin ----

    @GetMapping("/admin/posts")
    public Result<PageResult<Post>> listAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        return Result.success(postService.listAdmin(page, size, status, keyword));
    }

    @PostMapping("/admin/posts")
    public Result<Post> create(@Valid @RequestBody PostCreateRequest request) {
        return Result.success(postService.create(request));
    }

    @PutMapping("/admin/posts/{id}")
    public Result<Post> update(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        return Result.success(postService.update(id, request));
    }

    @DeleteMapping("/admin/posts/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return Result.success();
    }

    @GetMapping("/admin/posts/{id}")
    public Result<Post> getById(@PathVariable Long id) {
        return Result.success(postService.getById(id));
    }
}
