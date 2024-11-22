package com.itheima.mp.service;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/18 17:02
 * @description
 */
@SpringBootTest
class IUserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(5L);
        user.setUsername("LiLei");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
//        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
//        userMapper.saveUser(user);
        userService.save(user);
    }

    @Test
    void testQuery() {
        List<User> users = userService.listByIds(List.of(1L, 2L, 4L));
        users.forEach(System.out::println);
    }

    /**
     * 测试IService插入 性能
     * 一次插一个
     */
    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    /**
     * 测试IService批量插入 性能
     * 一次插多个
     */
    @Test
    void testSaveBatch() {
        // 准备10万条数据
        List<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            list.add(buildUser(i));
            // 每1000条批量插入一次
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                // 清空集合准备下一批数据
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    // 构建User对象
    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
//        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    /**
     * 使用分页插件进行分页查询
     */
    @Test
    void testPageQuery() {
        int pageNo = 1, pageSize = 2; // 第一页，一页查2条
        // 1. 准备分页条件
        // 1.1 分页条件
        Page<User> page = Page.of(pageNo, pageSize);
        // 1.2 排序条件
        page.addOrder(new OrderItem("balance", false)); // false代表降序
        page.addOrder(new OrderItem("id", true)); // order可以添加多个 如果balance相等根据id升序排

        // 2. 分页查询
        Page<User> p = userService.page(page);

        // 3. 解析
        System.out.println("JSONUtil.toJsonStr(p) = " + JSONUtil.toJsonStr(p));
        long total = p.getTotal(); // 查询到的总条数
        System.out.println("total = " + total);
        long pages = p.getPages(); // 查询到的总页数
        System.out.println("pages = " + pages);
        List<User> users = p.getRecords();
        users.forEach(System.out::println);
    }
}
