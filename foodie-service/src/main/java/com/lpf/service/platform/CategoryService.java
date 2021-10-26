package com.lpf.service.platform;

import com.lpf.pojo.Category;
import com.lpf.pojo.vo.CategoryVO;
import com.lpf.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有一级分类
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类id查询子分类信息
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每个一级分类下的6条最新商品数据
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
