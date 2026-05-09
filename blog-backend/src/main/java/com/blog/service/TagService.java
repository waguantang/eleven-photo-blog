package com.blog.service;

import com.blog.common.BizException;
import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.mapper.TagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class TagService {

    private final TagMapper tagMapper;
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");

    public TagService(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public List<Tag> list() {
        return tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().orderByAsc(Tag::getCreatedAt));
    }

    public Tag create(TagRequest req) {
        Tag tag = new Tag();
        tag.setName(req.getName());
        tag.setSlug(generateSlug(req.getName()));
        tagMapper.insert(tag);
        return tag;
    }

    public Tag update(Long id, TagRequest req) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) throw new BizException(404, "标签不存在");
        tag.setName(req.getName());
        tag.setSlug(generateSlug(req.getName()));
        tagMapper.updateById(tag);
        return tag;
    }

    public void delete(Long id) {
        tagMapper.deleteById(id);
    }

    private String generateSlug(String name) {
        String normalized = Normalizer.normalize(name.trim(), Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("\\s+", "-").replaceAll("^-|-$", "");
    }
}
