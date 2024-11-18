package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 自定义SQL
     * 传递where条件
     * 在mapper方法参数中用Param注解声明wrapper变量名称，必须是ew。`WRAPPER`就是`ew`
     * @param wrapper
     * @param amount
     */
    void updateBalanceByIds(@Param(Constants.WRAPPER) LambdaQueryWrapper<User> wrapper, @Param("amount") int amount);

    /**
     * 根据id扣减金额
     * @param id
     * @param money
     */
    @Update("update tb_user set balance = balance- #{money} where id = #{id}")
    void deductBalance(@Param("id") Long id, @Param("money") Integer money);
/*
    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(@Param("id") Long id);

    List<User> queryUserByIds(@Param("ids") List<Long> ids);
*/
}
