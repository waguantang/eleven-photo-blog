package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public Result<List<Tag>> list() {
        return Result.success(tagService.list());
    }

    @PostMapping("/admin/tags")
    public Result<Tag> create(@Valid @RequestBody TagRequest request) {
        return Result.success(tagService.create(request));
    }

    @PutMapping("/admin/tags/{id}")
    public Result<Tag> update(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        return Result.success(tagService.update(id, request));
    }

    @DeleteMapping("/admin/tags/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return Result.success();
    }
}
