package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
//        userMapper.saveUser(user);
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
//        User user = userMapper.queryUserById(5L);
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
//        List<User> users = userMapper.queryUserByIds(List.of(1L, 2L, 3L, 4L));
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
//        userMapper.updateUser(user);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
//        userMapper.deleteUser(5L);
        userMapper.deleteById(5L);
    }

    /**
     * 查询出名字中带`o`的，存款大于等于1000元的人。
     * QueryWrapper
     */
    @Test
    void testQueryWrapper() {
        // select id, username, info, balance from tb_user where name like "%o%" AND balance >= 1000
        // 1. 构建查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        // 2. 查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * Lambda方式查询出名字中带`o`的，存款大于等于1000元的人。
     * LambdaQueryWrapper
     */
    @Test
    void testLambdaQueryWrapper() {
        // 1. 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        // 2. 查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 更新用户名为jack的用户的余额为2000
     * QueryWrapper
     */
    @Test
    void testUpdateByQueryWrapper() {
        // 1. 要更新的数据 user中非null字段都会作为set语句
        User user = new User();
        user.setBalance(2000);

        // 2. 更新的条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", "Jack");

        // 3. 执行更新
        userMapper.update(user, wrapper);
    }

    /**
     * 更新id为`1,2,4`的用户的余额，扣200
     * SET的赋值结果是基于字段现有值的
     * UpdateWrapper
     */
    @Test
    void testUpdateWrapper() {
        List<Long> ids = List.of(1L, 2L, 4L);
        // UPDATE user SET balance = balance - 200 WHERE id in (1, 2, 4)
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200 ")
                        .in("id", ids);
        // 执行更新
        userMapper.update(null, wrapper);
    }

    /**
     * 更新id为`1,2,4`的用户的余额，扣200
     * 自定义SQL
     */
    @Test
    void testCustomSqlUpdate() {
        // 1. 更新条件
        List<Long> ids = List.of(1L, 2L, 4L);
        int amount = 200;
        // 2. 定义条件
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>()
                .lambda()
                .in(User::getId, ids);
        // 3. 调用自定义SQL方法
        userMapper.updateBalanceByIds(wrapper, amount);
    }
}