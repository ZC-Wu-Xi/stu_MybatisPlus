package com.itheima.mp.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/22 9:56
 * @description 返回的通用分页结果
 * 我们有可能返回VO也有可能返回DTO去调其他微服务
 */
@Data
@ApiModel(description = "分页结果")
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    @ApiModelProperty("总条数")
    private Long total;
    @ApiModelProperty("总页数")
    private Long pages;
    @ApiModelProperty("集合")
    private List<T> list;

    // 封装一些分页查询会用到的工具方法

    /**
     * 封装VO结果——分页查询
     * 将MybatisPlus分页结果转为 VO分页结果
     * 这个方法只能对两个类中属性名一样的进行拷贝
     * @param p MybatisPlus的分页结果
     * @param clazz 返回给前端的VO对象
     * @return
     * @param <PO>
     * @param <VO>
     */
    public static <PO, VO> PageDTO<VO> of(Page<PO> p, Class<VO> clazz) {
        // 封装VO结果
        PageDTO<VO> dto = new PageDTO<>();
        // 1. 总条数
        dto.setTotal(p.getTotal());
        // 2. 总页数
        dto.setPages(p.getPages());
        // 3. 当前页数据
        List<PO> records = p.getRecords();
        if (CollUtil.isEmpty(records)) {
            dto.setList(Collections.emptyList()); // 返回空集合
            return dto;
        }
        // 4. 拷贝VO
        dto.setList(BeanUtil.copyToList(records, clazz));

        // 5. 返回
        return dto;
    }

    /**
     * 封装VO结果——分页查询
     * 将MybatisPlus分页结果转为 VO分页结果
     * 传入拷贝的函数
     * @param p
     * @param convertor
     * @return
     * @param <PO>
     * @param <VO>
     */
    public static <PO, VO> PageDTO<VO> of(Page<PO> p, Function<PO, VO> convertor) {
        // 封装VO结果
        PageDTO<VO> dto = new PageDTO<>();
        // 1. 总条数
        dto.setTotal(p.getTotal());
        // 2. 总页数
        dto.setPages(p.getPages());
        // 3. 当前页数据
        List<PO> records = p.getRecords();
        if (CollUtil.isEmpty(records)) {
            dto.setList(Collections.emptyList()); // 返回空集合
            return dto;
        }
        // 4. 拷贝VO
        dto.setList(records.stream().map(convertor).collect(Collectors.toList()));
        // 5. 返回
        return dto;
    }

    /**
     * 返回空分页结果
     * 将MybatisPlus分页结果转为 VO分页结果
     * @param p
     * @return
     * @param <V>
     * @param <P>
     */
    public static <V, P> PageDTO<V> empty(Page<P> p){
        return new PageDTO<>(p.getTotal(), p.getPages(), Collections.emptyList());
    }
}
