package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/18 16:58
 * @description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 根据id扣减用户余额
     * @param id
     * @param money
     */
    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        // 1. 查询用户
        User user = getById(id);

        // 2. 校验用户状态  1:正常 2:冻结
        if (user == null || user.getStatus().equals("2")) {
            throw new RuntimeException("无该用户或用户状态异常！");
        }

        // 3. 校验余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足！");
        }

        // 4. 扣减金额 update tb_user set balance = balance = ?
//        baseMapper.deductBalance(id, money); // 自定义的mapper
        // 4. 扣减余额，且当扣减后余额为0时修改用户状态为2:冻结，
        int remainBalance = user.getBalance() - money; // 扣减后的余额
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, 2) // 动态判断，是否更新status
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) // 乐观锁 用户余额=刚才查到的余额
                .update();
    }

    /**
     * 复杂条件查询 IService的lambda方式实现
     * @param name
     * @param status
     * @param minBalance
     * @param maxBalance
     * @return
     */
    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .gt(minBalance != null, User::getBalance, minBalance)
                .lt(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }
}
