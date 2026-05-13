package com.blog.service;

import com.blog.common.BizException;
import com.blog.dto.PhotoRequest;
import com.blog.entity.Photo;
import com.blog.mapper.PhotoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {

    private final PhotoMapper photoMapper;

    public PhotoService(PhotoMapper photoMapper) {
        this.photoMapper = photoMapper;
    }

    public List<Photo> listAll() {
        return photoMapper.selectList(
                new LambdaQueryWrapper<Photo>()
                        .orderByAsc(Photo::getSortOrder)
                        .orderByDesc(Photo::getCreatedAt));
    }

    public Photo create(PhotoRequest req) {
        Photo photo = new Photo();
        photo.setTitle(req.getTitle());
        photo.setDescription(req.getDescription());
        photo.setSrc(req.getSrc());
        photo.setCategory(req.getCategory());
        photo.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        photoMapper.insert(photo);
        return photo;
    }

    public Photo update(Long id, PhotoRequest req) {
        Photo photo = photoMapper.selectById(id);
        if (photo == null) throw new BizException(404, "照片不存在");
        photo.setTitle(req.getTitle());
        photo.setDescription(req.getDescription());
        photo.setSrc(req.getSrc());
        photo.setCategory(req.getCategory());
        if (req.getSortOrder() != null) photo.setSortOrder(req.getSortOrder());
        photoMapper.updateById(photo);
        return photo;
    }

    public void delete(Long id) {
        photoMapper.deleteById(id);
    }
}
