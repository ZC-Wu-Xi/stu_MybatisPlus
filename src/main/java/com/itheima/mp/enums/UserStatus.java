package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/21 21:17
 * @description 用户状态枚举类
 */
@Getter
public enum UserStatus {
    NORMAL(1, "正常"),
    FROZEN(2, "冻结"),
    ;

    @EnumValue // 标记枚举中的哪个字段的值作为数据库值
    private final int value;
    // 标记JSON序列化时展示的字段 即1或2
    // 如果不加@JsonValue前端查到的数据则为"NORMAL"或"FROZEN"
    @JsonValue // 我们返回给前端值时SpringMvc的jackson包来处理json 加上该注解，返回给前端该类时只返回该属性，即"正常"或"冻结"
    private final String desc;
    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
