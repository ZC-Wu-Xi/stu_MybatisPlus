package com.itheima.mp.domain.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/22 9:49
 * @description 分页查询的父类
 */
@Data
@ApiModel(description = "分页查询实体")
public class PageQuery {
    @ApiModelProperty("页码")
    private Integer pageNo = 1;
    @ApiModelProperty("页码")
    private Integer pageSize = 5;
    @ApiModelProperty("排序字段")
    private String sortBy;
    @ApiModelProperty("是否升序")
    private boolean isAsc = true;

    /**
     * 构建分页条件 传入多个排序方式
     * @param items 排序方式 可变参数
     * @return
     * @param <T>
     */
    public <T> Page<T> toMPPage(OrderItem... items) {
        // 1. 分页条件
        Page<T> page = Page.of(pageNo, pageSize);
        // 2. 构建排序条件
        if (StrUtil.isNotBlank(sortBy)) { // 排序条件不为空
            page.addOrder(new OrderItem(sortBy, isAsc));
        } else if (items != null){ // 排序条件为空
            // 默认排序方式
            page.addOrder(items);
        }
        return page;
    }

    /**
     * 构建分页条件 传入排序字段和排序方式
     * @param defaultSortBy 排序字段
     * @param defaultAsc 排序方式
     * @return
     * @param <T>
     */
    public <T> Page<T> toMPPage(String defaultSortBy, Boolean defaultAsc) {
        return toMPPage(new OrderItem(defaultSortBy, defaultAsc));
    }

    /**
     * 构建分页条件 默认按创建时间排序
     * @return
     * @param <T>
     */
    public <T> Page<T> toMPPageDefaultSortByCreateTime() {
        return toMPPage(new OrderItem("create_time", false));
    }

    /**
     * 构建分页条件 默认按修改时间排序
     * @return
     * @param <T>
     */
    public <T> Page<T> toMPPageDefaultSortByUpdateTime() {
        return toMPPage(new OrderItem("update_time", false));
    }
}
