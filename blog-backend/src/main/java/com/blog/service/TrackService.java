package com.blog.service;

import com.blog.common.BizException;
import com.blog.dto.TrackRequest;
import com.blog.entity.Track;
import com.blog.mapper.TrackMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackMapper trackMapper;

    public TrackService(TrackMapper trackMapper) {
        this.trackMapper = trackMapper;
    }

    public List<Track> listAll() {
        return trackMapper.selectList(
                new LambdaQueryWrapper<Track>()
                        .orderByAsc(Track::getSortOrder)
                        .orderByDesc(Track::getCreatedAt));
    }

    public Track create(TrackRequest req) {
        Track track = new Track();
        track.setTitle(req.getTitle());
        track.setArtist(req.getArtist());
        track.setUrl(req.getUrl());
        track.setCover(req.getCover());
        track.setDuration(req.getDuration());
        track.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        trackMapper.insert(track);
        return track;
    }

    public Track update(Long id, TrackRequest req) {
        Track track = trackMapper.selectById(id);
        if (track == null) throw new BizException(404, "曲目不存在");
        track.setTitle(req.getTitle());
        track.setArtist(req.getArtist());
        track.setUrl(req.getUrl());
        track.setCover(req.getCover());
        track.setDuration(req.getDuration());
        if (req.getSortOrder() != null) track.setSortOrder(req.getSortOrder());
        trackMapper.updateById(track);
        return track;
    }

    public void delete(Long id) {
        trackMapper.deleteById(id);
    }
}
