package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZC_Wu 汐
 * @date 2024/11/18 17:23
 * @description
 */
@Api("用户管理接口")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor // lombok必备参数的构造函数，该构造函数包含所有被 final 修饰的字段，以及所有被 @NonNull 注解修饰的字段
public class UserController {
//    @Autowired // 不推荐使用@Autowired注入，推荐使用构造方法注入
    private final IUserService userService; // 建议使用构造函数注入，可以使用final修饰保证不可变性

    /*
    // @RequiredArgsConstructor注解生成的构造函数
    public UserController(IUserService userService) {
        this.userService = userService;
    }
    */

    @ApiOperation("新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        // 1. 把DTO拷贝到PO
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        // 2. 新增
        userService.save(user); // IService中mp帮我们写好的方法
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUser(@ApiParam("用户id") @PathVariable("id") Long id) {
        // 删除
        userService.removeById(id); // IService中mp帮我们写好的方法
    }

    @ApiOperation("根据id查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUserById(@ApiParam("用户id") @PathVariable("id") Long id) {
        // 1. 查询用户
        User user = userService.getById(id);// IService中mp帮我们写好的方法
        // 2. 将PO拷贝到VO返回
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @ApiOperation("根据id批量查询用户接口")
    @GetMapping
    public List<UserVO> queryUserByIds(@ApiParam("用户id集合") @RequestParam("ids") List<Long> ids) {
        // 1. 查询用户
        List<User> users = userService.listByIds(ids);// IService中mp帮我们写好的方法
        // 2. 将PO集合拷贝到VO集合返回
//        List<UserVO> userVOS = BeanUtil.copyToList(users, UserVO.class);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    /**
     * 根据id扣减用户余额
     * 有业务逻辑 使用自定义service方法
     * @param id
     * @param money
     */
    @ApiOperation("扣减用户余额接口")
    @PutMapping("/{id}/deduction/{money}")
    public void deductBalance (
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减金额") @PathVariable("money") Integer money) {
        userService.deductBalance(id, money);// 有业务逻辑 我们自定义的service方法
    }

    @ApiOperation("根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery query) {
        // 1. 查询用户
        List<User> users = userService.queryUsers(
                query.getName(), query.getStatus(), query.getMinBalance(), query.getMaxBalance());
        // 2. 将PO集合拷贝到VO集合返回
        return BeanUtil.copyToList(users, UserVO.class);
    }
}
