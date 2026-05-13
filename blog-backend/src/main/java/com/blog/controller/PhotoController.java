package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.PhotoRequest;
import com.blog.entity.Photo;
import com.blog.service.PhotoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/photos")
    public Result<List<Photo>> list() {
        return Result.success(photoService.listAll());
    }

    @GetMapping("/admin/photos")
    public Result<List<Photo>> listAdmin() {
        return Result.success(photoService.listAll());
    }

    @PostMapping("/admin/photos")
    public Result<Photo> create(@Valid @RequestBody PhotoRequest request) {
        return Result.success(photoService.create(request));
    }

    @PutMapping("/admin/photos/{id}")
    public Result<Photo> update(@PathVariable Long id, @Valid @RequestBody PhotoRequest request) {
        return Result.success(photoService.update(id, request));
    }

    @DeleteMapping("/admin/photos/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        photoService.delete(id);
        return Result.success();
    }
}
