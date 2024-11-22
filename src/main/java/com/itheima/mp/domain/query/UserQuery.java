package com.itheima.mp.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询条件实体
 * 复杂条件查询
 */
@EqualsAndHashCode(callSuper = true) // 自动生成 equals() 和 hashCode() 方法，其中 callSuper = true 的参数指定了在生成这些方法时，父类的 equals() 和 hashCode() 方法也要被调用
@Data
@ApiModel(description = "用户查询条件实体")
public class UserQuery extends PageQuery{
    @ApiModelProperty("用户名关键字")
    private Integer pageSize;
    @ApiModelProperty("用户名关键字")
    private String name;
    @ApiModelProperty("用户状态：1-正常，2-冻结")
    private Integer status;
    @ApiModelProperty("余额最小值")
    private Integer minBalance;
    @ApiModelProperty("余额最大值")
    private Integer maxBalance;
}
