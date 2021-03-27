package com.lpf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lpf.mapper.CategoryMapper;
import com.lpf.pojo.Category;
import com.lpf.pojo.vo.CategoryVO;
import com.lpf.pojo.vo.NewItemsVO;
import com.lpf.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liupf
 * @date 2021-03-27 23:20
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Category> queryAllRootLevelCat() {

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 1);

        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapper.getSubCatList(rootCatId);
    }

    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {

        Map<String, Object> map = new HashMap<>();
        map.put("rootCatId", rootCatId);

        return categoryMapper.getSixNewItemsLazy(map);
    }
}
