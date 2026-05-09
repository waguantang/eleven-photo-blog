package com.blog.service;

import com.blog.common.BizException;
import com.blog.dto.CategoryRequest;
import com.blog.entity.Category;
import com.blog.mapper.CategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> list() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().orderByAsc(Category::getCreatedAt));
    }

    public Category create(CategoryRequest req) {
        Category cat = new Category();
        cat.setName(req.getName());
        cat.setSlug(generateSlug(req.getName()));
        cat.setDescription(req.getDescription());
        categoryMapper.insert(cat);
        return cat;
    }

    public Category update(Long id, CategoryRequest req) {
        Category cat = categoryMapper.selectById(id);
        if (cat == null) throw new BizException(404, "分类不存在");
        cat.setName(req.getName());
        cat.setSlug(generateSlug(req.getName()));
        cat.setDescription(req.getDescription());
        categoryMapper.updateById(cat);
        return cat;
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    private String generateSlug(String name) {
        String normalized = Normalizer.normalize(name.trim(), Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("\\s+", "-").replaceAll("^-|-$", "");
    }
}
