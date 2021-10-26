package com.lpf.controller.platform;

import com.lpf.consts.PageConsts;
import com.lpf.pojo.Items;
import com.lpf.pojo.ItemsImg;
import com.lpf.pojo.ItemsParam;
import com.lpf.pojo.ItemsSpec;
import com.lpf.pojo.vo.CommentLevelCountsVO;
import com.lpf.pojo.vo.ItemInfoVO;
import com.lpf.pojo.vo.ShopcartVO;
import com.lpf.service.platform.ItemService;
import com.lpf.utils.PagedGridResult;
import com.lpf.utils.ResultJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ResultJSONResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return ResultJSONResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return ResultJSONResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public ResultJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return ResultJSONResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return ResultJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public ResultJSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级")
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", defaultValue = PageConsts.COMMON_PAGE_SIZE_STR)
            @RequestParam(defaultValue = PageConsts.COMMON_PAGE_SIZE_STR) Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return ResultJSONResult.errorMsg(null);
        }
        PagedGridResult grid = itemService.queryPagedComments(itemId, level, page, pageSize);
        return ResultJSONResult.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ResultJSONResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", defaultValue = PageConsts.PAGE_SIZE_STR)
            @RequestParam(defaultValue = PageConsts.PAGE_SIZE_STR) Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            return ResultJSONResult.errorMsg(null);
        }

        PagedGridResult grid = itemService.searhItems(keywords, sort, page, pageSize);
        return ResultJSONResult.ok(grid);
    }

    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public ResultJSONResult catItems(
            @ApiParam(name = "catId", value = "三级分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页")
            @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
            @RequestParam(defaultValue = PageConsts.PAGE_SIZE_STR) Integer pageSize) {

        if (catId == null) {
            return ResultJSONResult.errorMsg(null);
        }

        PagedGridResult grid = itemService.searhItems(catId, sort, page, pageSize);
        return ResultJSONResult.ok(grid);
    }

    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public ResultJSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            return ResultJSONResult.ok();
        }

        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return ResultJSONResult.ok(list);
    }
}
