package com.lpf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lpf.pojo.Category;
import com.lpf.pojo.vo.CategoryVO;
import com.lpf.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryVO> getSubCatList(int rootCatId);

    List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}