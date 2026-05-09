package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.BizException;
import com.blog.common.PageResult;
import com.blog.dto.PostCreateRequest;
import com.blog.dto.PostUpdateRequest;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.Tag;
import com.blog.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class PostService {

    private final PostMapper postMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final PostTagMapper postTagMapper;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private static final Pattern DASHES = Pattern.compile("[-]+");

    public PostService(PostMapper postMapper, CategoryMapper categoryMapper,
                       TagMapper tagMapper, PostTagMapper postTagMapper) {
        this.postMapper = postMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.postTagMapper = postTagMapper;
    }

    public PageResult<Post> listPublic(int page, int size, Long categoryId, Long tagId, String keyword) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getStatus, "published")
                .orderByDesc(Post::getPublishedAt);
        if (categoryId != null) wrapper.eq(Post::getCategoryId, categoryId);
        if (keyword != null && !keyword.isEmpty()) wrapper.like(Post::getTitle, keyword);

        Page<Post> pg = postMapper.selectPage(new Page<>(page, size), wrapper);
        // For tag filter, filter after main query
        List<Post> records = pg.getRecords();
        if (tagId != null) {
            records = records.stream()
                    .filter(p -> tagMapper.selectByPostId(p.getId()).stream().anyMatch(t -> t.getId().equals(tagId)))
                    .toList();
        }
        enrichPosts(records);
        return PageResult.of(records, pg.getTotal(), page, size);
    }

    public PageResult<Post> listAdmin(int page, int size, String status, String keyword) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                .orderByDesc(Post::getCreatedAt);
        if (status != null && !status.isEmpty()) wrapper.eq(Post::getStatus, status);
        if (keyword != null && !keyword.isEmpty()) wrapper.like(Post::getTitle, keyword);

        Page<Post> pg = postMapper.selectPage(new Page<>(page, size), wrapper);
        enrichPosts(pg.getRecords());
        return PageResult.of(pg.getRecords(), pg.getTotal(), page, size);
    }

    public Post getBySlug(String slug) {
        Post post = postMapper.selectBySlug(slug);
        if (post == null) throw new BizException(404, "文章不存在");
        enrichPost(post);
        return post;
    }

    public Post getById(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) throw new BizException(404, "文章不存在");
        enrichPost(post);
        return post;
    }

    @Transactional
    public Post create(PostCreateRequest req) {
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setSlug(generateSlug(req.getTitle()));
        post.setContent(req.getContent());
        post.setExcerpt(req.getExcerpt());
        post.setCoverImage(req.getCoverImage());
        post.setStatus(req.getStatus());
        post.setCategoryId(req.getCategoryId());
        if ("published".equals(req.getStatus())) {
            post.setPublishedAt(LocalDateTime.now());
        }
        postMapper.insert(post);
        if (req.getTagIds() != null) {
            for (Long tagId : req.getTagIds()) {
                postTagMapper.insert(post.getId(), tagId);
            }
        }
        return getById(post.getId());
    }

    @Transactional
    public Post update(Long id, PostUpdateRequest req) {
        Post post = postMapper.selectById(id);
        if (post == null) throw new BizException(404, "文章不存在");

        post.setTitle(req.getTitle());
        if (!req.getTitle().equals(post.getTitle())) {
            post.setSlug(generateSlug(req.getTitle()));
        }
        post.setContent(req.getContent());
        post.setExcerpt(req.getExcerpt());
        post.setCoverImage(req.getCoverImage());
        post.setCategoryId(req.getCategoryId());
        if (req.getStatus() != null) {
            post.setStatus(req.getStatus());
            if ("published".equals(req.getStatus()) && post.getPublishedAt() == null) {
                post.setPublishedAt(LocalDateTime.now());
            }
        }
        postMapper.updateById(post);

        // Update tags
        postTagMapper.deleteByPostId(id);
        if (req.getTagIds() != null) {
            for (Long tagId : req.getTagIds()) {
                postTagMapper.insert(id, tagId);
            }
        }
        return getById(id);
    }

    @Transactional
    public void delete(Long id) {
        postTagMapper.deleteByPostId(id);
        postMapper.deleteById(id);
    }

    public Post getPrev(Long id) {
        return postMapper.selectPrev(id);
    }

    public Post getNext(Long id) {
        return postMapper.selectNext(id);
    }

    public List<String> getArchiveMonths() {
        return postMapper.selectArchiveMonths();
    }

    private void enrichPosts(List<Post> posts) {
        for (Post p : posts) enrichPost(p);
    }

    private void enrichPost(Post p) {
        if (p.getCategoryId() != null) {
            p.setCategory(categoryMapper.selectById(p.getCategoryId()));
        }
        p.setTags(tagMapper.selectByPostId(p.getId()));
    }

    private String generateSlug(String title) {
        String nowhitespace = WHITESPACE.matcher(title.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = DASHES.matcher(slug).replaceAll("-");
        slug = slug.toLowerCase(Locale.ENGLISH).replaceAll("^-|-$", "");
        if (slug.isEmpty()) slug = "post";
        // Append timestamp to avoid duplicates
        return slug + "-" + System.currentTimeMillis();
    }
}
