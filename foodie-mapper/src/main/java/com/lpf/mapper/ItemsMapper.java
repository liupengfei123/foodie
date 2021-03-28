package com.lpf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lpf.pojo.Items;
import com.lpf.pojo.vo.ItemCommentVO;
import com.lpf.pojo.vo.SearchItemsVO;
import com.lpf.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ItemsMapper extends BaseMapper<Items> {

    List<ItemCommentVO> queryItemComments(Page<?> page, @Param("paramsMap") Map<String, Object> map);

    List<SearchItemsVO> searchItems(Page<?> page, @Param("paramsMap") Map<String, Object> map);

    List<SearchItemsVO> searchItemsByThirdCat(Page<?> page, @Param("paramsMap") Map<String, Object> map);

    List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    int decreaseItemSpecStock(@Param("specId") String specId, @Param("pendingCounts") int pendingCounts);
}