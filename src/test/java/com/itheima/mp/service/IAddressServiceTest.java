package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/21 20:53
 * @description
 */
@SpringBootTest
class IAddressServiceTest {
    @Autowired
    private IAddressService addressService;

    /**
     * 测试逻辑删除 已在配置文件中配置
     */
    @Test
    void testLogicDelete() {
        // 方法与普通删除一模一样，但是底层的SQL逻辑变了

        // 删除
        addressService.removeById(59L);

        // 查询
        Address address = addressService.getById(59L);
        System.out.println("address = " + address);
    }
}