package com.lpf.pojo.vo;

import com.lpf.pojo.Items;
import com.lpf.pojo.ItemsImg;
import com.lpf.pojo.ItemsParam;
import com.lpf.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * 商品详情VO
 */
@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

}
