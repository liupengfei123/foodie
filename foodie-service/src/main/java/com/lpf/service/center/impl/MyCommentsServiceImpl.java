package com.lpf.service.center.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lpf.enums.YesOrNo;
import com.lpf.mapper.ItemsCommentsMapper;
import com.lpf.mapper.OrderItemsMapper;
import com.lpf.mapper.OrderStatusMapper;
import com.lpf.mapper.OrdersMapper;
import com.lpf.pojo.OrderItems;
import com.lpf.pojo.OrderStatus;
import com.lpf.pojo.Orders;
import com.lpf.pojo.bo.center.OrderItemsCommentBO;
import com.lpf.pojo.vo.MyCommentVO;
import com.lpf.service.center.MyCommentsService;
import com.lpf.service.util.PageUtils;
import com.lpf.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    @Autowired
    public OrderItemsMapper orderItemsMapper;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    @Autowired
    public ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems entity = new OrderItems();
        entity.setOrderId(orderId);

        QueryWrapper<OrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(entity);
        return orderItemsMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId,
                             List<OrderItemsCommentBO> commentList) {

        // 1. 保存评价 items_comments
        for (OrderItemsCommentBO oic : commentList) {
            oic.setCommentId(sid.nextShort());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapper.saveComments(map);

        // 2. 修改订单表改已评价 orders
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateById(order);

        // 3. 修改订单状态表的留言时间 order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateById(orderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        Page<MyCommentVO> pageInfo = new Page<>(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapper.queryMyComments(pageInfo, map);
        pageInfo.setRecords(list);

        return PageUtils.setterPagedGrid(pageInfo, page);
    }
}
