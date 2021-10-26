package com.lpf.service.platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lpf.enums.CommentLevel;
import com.lpf.enums.YesOrNo;
import com.lpf.mapper.*;
import com.lpf.pojo.*;
import com.lpf.pojo.vo.CommentLevelCountsVO;
import com.lpf.pojo.vo.ItemCommentVO;
import com.lpf.pojo.vo.SearchItemsVO;
import com.lpf.pojo.vo.ShopcartVO;
import com.lpf.service.platform.ItemService;
import com.lpf.service.util.PageUtils;
import com.lpf.utils.DesensitizationUtil;
import com.lpf.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectById(itemId);
    }

    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        QueryWrapper<ItemsImg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("itemId", itemId);
        return itemsImgMapper.selectList(queryWrapper);
    }

    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        QueryWrapper<ItemsSpec> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("itemId", itemId);
        return itemsSpecMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        QueryWrapper<ItemsParam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("itemId", itemId);
        return itemsParamMapper.selectOne(queryWrapper);
    }

    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setTotalCounts(totalCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setBadCounts(badCounts);

        return countsVO;
    }

    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);
        if (level != null) {
            itemsComments.setCommentLevel(level);
        }

        QueryWrapper<ItemsComments> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemsComments);
        return itemsCommentsMapper.selectCount(queryWrapper);
    }

    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);

        Page<ItemCommentVO> pageInfo = new Page<>(page, pageSize);
        List<ItemCommentVO> list = itemsMapper.queryItemComments(pageInfo, map);
        for (ItemCommentVO vo : list) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }
        pageInfo.setRecords(list);
        return PageUtils.setterPagedGrid(pageInfo, page);
    }


    @Override
    public PagedGridResult searhItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);
        Page<SearchItemsVO> pageInfo = new Page<>(page, pageSize);
        List<SearchItemsVO> list = itemsMapper.searchItems(pageInfo, map);
        pageInfo.setRecords(list);
        return PageUtils.setterPagedGrid(pageInfo, page);
    }

    @Override
    public PagedGridResult searhItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);

        Page<SearchItemsVO> pagInfo = new Page<>(page, pageSize);
        List<SearchItemsVO> list = itemsMapper.searchItemsByThirdCat(pagInfo, map);
        pagInfo.setRecords(list);
        return PageUtils.setterPagedGrid(pagInfo, page);
    }

    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String ids[] = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids);

        return itemsMapper.queryItemsBySpecIds(specIdsList);
    }

    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectById(specId);
    }

    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);

        ItemsImg result = itemsImgMapper.selectOne(new QueryWrapper<>(itemsImg));
        return result != null ? result.getUrl() : "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库: 不推荐，导致数据库性能低下
        // 分布式锁 zookeeper redis

        // lockUtil.getLock(); -- 加锁
        // lockUtil.unLock(); -- 解锁

        int result = itemsMapper.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }
}
