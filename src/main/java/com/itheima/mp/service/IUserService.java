package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;

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

    /**
     * 据id用户查询的接口，查询用户的同时返回用户收货地址列表
     * @param id
     * @return
     */
    UserVO queryUserAndAddressById(Long id);

    /**
     * 根据id批量查询用户，并查询出用户对应的所有地址
     * @param ids
     * @return
     */
    List<UserVO> queryUserAndAddressByIds(List<Long> ids);
}
