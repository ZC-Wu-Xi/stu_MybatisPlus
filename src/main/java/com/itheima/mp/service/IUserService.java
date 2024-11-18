package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;

import java.util.List;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/18 16:58
 * @description
 */
public interface IUserService extends IService<User> {
    /**
     * 根据id扣减用户余额
     * @param id
     * @param money
     */
    void deductBalance(Long id, Integer money);

    /**
     * 根据复杂条件查询
     * @param name
     * @param status
     * @param minBalance
     * @param maxBalance
     * @return
     */
    List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance);
}
