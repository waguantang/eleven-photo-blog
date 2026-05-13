package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.SiteConfigRequest;
import com.blog.entity.SiteConfig;
import com.blog.service.SiteConfigService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SiteConfigController {

    private final SiteConfigService siteConfigService;

    public SiteConfigController(SiteConfigService siteConfigService) {
        this.siteConfigService = siteConfigService;
    }

    @GetMapping("/config")
    public Result<Map<String, String>> list() {
        return Result.success(siteConfigService.listAll());
    }

    @GetMapping("/admin/config")
    public Result<List<SiteConfig>> listAdmin() {
        return Result.success(siteConfigService.listAdmin());
    }

    @PostMapping("/admin/config")
    public Result<SiteConfig> create(@Valid @RequestBody SiteConfigRequest request) {
        return Result.success(siteConfigService.create(request));
    }

    @PutMapping("/admin/config/{id}")
    public Result<SiteConfig> update(@PathVariable Long id, @Valid @RequestBody SiteConfigRequest request) {
        return Result.success(siteConfigService.update(id, request));
    }

    @DeleteMapping("/admin/config/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        siteConfigService.delete(id);
        return Result.success();
    }
}
