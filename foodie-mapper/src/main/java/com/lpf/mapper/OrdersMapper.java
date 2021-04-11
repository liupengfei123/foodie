package com.lpf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lpf.pojo.OrderStatus;
import com.lpf.pojo.Orders;
import com.lpf.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrdersMapper extends BaseMapper<Orders> {
    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}