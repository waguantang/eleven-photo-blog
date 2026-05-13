package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.SiteConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SiteConfigMapper extends BaseMapper<SiteConfig> {

    @Select("SELECT * FROM t_site_config WHERE config_key = #{key}")
    SiteConfig selectByKey(String key);
}
