package com.lpf.service.center.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lpf.enums.OrderStatusEnum;
import com.lpf.enums.YesOrNo;
import com.lpf.mapper.OrderStatusMapper;
import com.lpf.mapper.OrdersMapper;
import com.lpf.pojo.OrderStatus;
import com.lpf.pojo.Orders;
import com.lpf.pojo.vo.MyOrdersVO;
import com.lpf.pojo.vo.OrderStatusCountsVO;
import com.lpf.service.center.MyOrdersService;
import com.lpf.service.util.PageUtils;
import com.lpf.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyOrdersServiceImpl implements MyOrdersService {


    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }

        Page<MyOrdersVO> pageInfo = new Page<>(page, pageSize);
        List<MyOrdersVO> list = ordersMapper.queryMyOrders(pageInfo, map);
        pageInfo.setRecords(list);

        return PageUtils.setterPagedGrid(pageInfo, page);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());



        UpdateWrapper<OrderStatus> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("orderId", orderId);
        updateWrapper.eq("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        orderStatusMapper.update(updateOrder, updateWrapper);
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.NO.type);

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(orders);
        return ordersMapper.selectOne(queryWrapper);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        updateOrder.setSuccessTime(new Date());

        UpdateWrapper<OrderStatus> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("orderId", orderId);
        updateWrapper.eq("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        int result = orderStatusMapper.update(updateOrder, updateWrapper);

        return result == 1 ? true : false;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {

        Orders updateOrder = new Orders();
        updateOrder.setIsDelete(YesOrNo.YES.type);
        updateOrder.setUpdatedTime(new Date());

        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", orderId);
        updateWrapper.eq("userId", userId);

        int result = ordersMapper.update(updateOrder, updateWrapper);

        return result == 1 ? true : false;
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.NO.type);
        int waitCommentCounts = ordersMapper.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts,
                                                            waitDeliverCounts,
                                                            waitReceiveCounts,
                                                            waitCommentCounts);
        return countsVO;
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        Page<OrderStatus> pageInfo = new Page<>(page, pageSize);
        List<OrderStatus> list = ordersMapper.getMyOrderTrend(pageInfo, map);
        pageInfo.setRecords(list);

        return PageUtils.setterPagedGrid(pageInfo, page);
    }
}
