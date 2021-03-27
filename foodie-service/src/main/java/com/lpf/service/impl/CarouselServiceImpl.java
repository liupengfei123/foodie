package com.lpf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lpf.mapper.CarouselMapper;
import com.lpf.pojo.Carousel;
import com.lpf.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liupf
 * @date 2021-03-27 23:21
 */
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> queryAll(Integer isShow) {

        QueryWrapper<Carousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_show", isShow);
        queryWrapper.orderByDesc("sort");
        return carouselMapper.selectList(queryWrapper);
    }
}
