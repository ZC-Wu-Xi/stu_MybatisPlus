package com.itheima.mp.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/21 21:51
 * @description User的userInfo属性类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of") // 提供静态方法
public class UserInfo {
    private Integer age;
    private String intro;
    private String gender;
}
