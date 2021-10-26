package com.lpf.service.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lpf.utils.PagedGridResult;

/**
 * @author liupf
 * @date 2021-10-26 22:06
 */
public class PageUtils {

    public static PagedGridResult setterPagedGrid(Page<?> pagInfo, Integer page) {
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(pagInfo.getRecords());
        grid.setTotal(pagInfo.getPages());
        grid.setRecords(pagInfo.getTotal());
        return grid;
    }
}
