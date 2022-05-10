package com.lpf.pojo.vo;

import com.lpf.pojo.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;


    private List<ShopcartBO> toBeRemovedShopcatdList;
}