package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.itheima.mp.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName(value = "tb_user", autoResultMap = true) // 因为该类内部有一个json处理器处理的类，属于较复杂的，需要开启自动结果映射，autoResultMap = true
public class User {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO) // 指定id为自增长 如果不指定自增长且插入时未指定值，则根据雪花算法算出一个id，一个较长的long类型
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册手机号
     */
    private String phone;

    /**
     * 详细信息
     * 数据库中是json类型
     */
//    private String info;
    @TableField(typeHandler = JacksonTypeHandler.class) // 开启json类型处理器
    private UserInfo info;

    /**
     * 使用状态（1正常 2冻结）
     */
//    private Integer status;
    private UserStatus status; // 配置枚举处理器后

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
