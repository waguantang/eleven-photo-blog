package com.blog.service;

import com.blog.common.BizException;
import com.blog.dto.SiteConfigRequest;
import com.blog.entity.SiteConfig;
import com.blog.mapper.SiteConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SiteConfigService {

    private final SiteConfigMapper siteConfigMapper;

    public SiteConfigService(SiteConfigMapper siteConfigMapper) {
        this.siteConfigMapper = siteConfigMapper;
    }

    public Map<String, String> listAll() {
        return siteConfigMapper.selectList(
                new LambdaQueryWrapper<SiteConfig>().orderByAsc(SiteConfig::getId))
                .stream()
                .collect(Collectors.toMap(SiteConfig::getConfigKey, SiteConfig::getConfigValue));
    }

    public List<SiteConfig> listAdmin() {
        return siteConfigMapper.selectList(
                new LambdaQueryWrapper<SiteConfig>().orderByAsc(SiteConfig::getId));
    }

    public SiteConfig create(SiteConfigRequest req) {
        SiteConfig existing = siteConfigMapper.selectByKey(req.getConfigKey());
        if (existing != null) throw new BizException(400, "配置键已存在");

        SiteConfig config = new SiteConfig();
        config.setConfigKey(req.getConfigKey());
        config.setConfigValue(req.getConfigValue());
        siteConfigMapper.insert(config);
        return config;
    }

    public SiteConfig update(Long id, SiteConfigRequest req) {
        SiteConfig config = siteConfigMapper.selectById(id);
        if (config == null) throw new BizException(404, "配置不存在");
        config.setConfigValue(req.getConfigValue());
        siteConfigMapper.updateById(config);
        return config;
    }

    public void delete(Long id) {
        siteConfigMapper.deleteById(id);
    }
}
