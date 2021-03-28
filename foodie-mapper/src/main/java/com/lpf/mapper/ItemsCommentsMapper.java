package com.lpf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lpf.pojo.ItemsComments;
import com.lpf.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ItemsCommentsMapper extends BaseMapper<ItemsComments> {
    void saveComments(Map<String, Object> map);

    List<MyCommentVO> queryMyComments(Page<?> page, @Param("paramsMap") Map<String, Object> map);
}