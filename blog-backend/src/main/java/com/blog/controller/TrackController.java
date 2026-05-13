package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.TrackRequest;
import com.blog.entity.Track;
import com.blog.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/tracks")
    public Result<List<Track>> list() {
        return Result.success(trackService.listAll());
    }

    @GetMapping("/admin/tracks")
    public Result<List<Track>> listAdmin() {
        return Result.success(trackService.listAll());
    }

    @PostMapping("/admin/tracks")
    public Result<Track> create(@Valid @RequestBody TrackRequest request) {
        return Result.success(trackService.create(request));
    }

    @PutMapping("/admin/tracks/{id}")
    public Result<Track> update(@PathVariable Long id, @Valid @RequestBody TrackRequest request) {
        return Result.success(trackService.update(id, request));
    }

    @DeleteMapping("/admin/tracks/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        trackService.delete(id);
        return Result.success();
    }
}
