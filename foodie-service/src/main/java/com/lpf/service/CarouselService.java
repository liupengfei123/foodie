package com.lpf.service;

import com.lpf.pojo.Carousel;

import java.util.List;

public interface CarouselService {

    /**
     * 查询所有轮播图列表
     * @param isShow 是否展示
     * @return 轮播图信息
     */
    List<Carousel> queryAll(Integer isShow);
}
