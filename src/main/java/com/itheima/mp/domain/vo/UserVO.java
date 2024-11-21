package com.itheima.mp.domain.vo;

import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户VO实体
 * 根据id用户查询的接口，查询用户的同时返回用户收货地址列表
 */
@Data
@ApiModel(description = "用户VO实体")
public class UserVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("详细信息")
//    private String info;
    private UserInfo info;

    @ApiModelProperty("使用状态（1正常 2冻结）")
//    private Integer status;
    private UserStatus status;

    @ApiModelProperty("账户余额")
    private Integer balance;

    // 静态工具Db类案例的新增字段
    @ApiModelProperty("用户的收货地址")
    private List<AddressVO> address;
}
