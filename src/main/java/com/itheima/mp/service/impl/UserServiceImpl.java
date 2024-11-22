package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
//        if (user == null || user.getStatus().equals("2")) {
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
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
//                .set(remainBalance == 0, User::getStatus, 2) // 动态判断，是否更新status
                .set(remainBalance == 0, User::getStatus, UserStatus.FROZEN) // 动态判断，是否更新status
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

    /**
     * 复杂条件分页查询
     * @param query
     * @return
     */
    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery query) {
        String name = query.getName();
        Integer status = query.getStatus();
        Integer minBalance = query.getMinBalance();
        Integer maxBalance = query.getMaxBalance();

        /*
        // 1. 构建分页条件
        // 1.1 分页条件
        Page<User> page = Page.of(query.getPageNo(), query.getPageSize());
        // 1.2 构建排序条件
        if (StrUtil.isNotBlank(query.getSortBy())) { // 排序条件不为空
            page.addOrder(new OrderItem(query.getSortBy(), query.isAsc()));
        } else { // 排序条件为空
            // 默认按照更新时间降序排
            page.addOrder(new OrderItem("update_time", false));
        }
        */
        // 1. 构建分页条件
        Page<User> page = query.toMPPageDefaultSortByUpdateTime();

        // 2. 分页查询
        Page<User> p = lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .gt(minBalance != null, User::getBalance, minBalance)
                .lt(maxBalance != null, User::getBalance, maxBalance)
                .page(page);

        /*
        // 3. 封装VO结果
        PageDTO<UserVO> dto = new PageDTO<>();
        // 3.1 总条数
        dto.setTotal(p.getTotal());
        // 3.2 总页数
        dto.setPages(p.getPages());
        // 3.3 当前页数据
        List<User> records = p.getRecords();
        if (CollUtil.isEmpty(records)) {
            dto.setList(Collections.emptyList()); // 返回空集合
            return dto;
        }
        // 3.4 拷贝UserVO
        dto.setList(BeanUtil.copyToList(records, UserVO.class));

        // 4. 返回
        return dto;
        */
        // 3. 封装VO结果
//        return PageDTO.of(page, UserVO.class);
//        return PageDTO.of(p, user -> BeanUtil.copyProperties(user, UserVO.class));
        return PageDTO.of(p, user -> {
            // 1. 拷贝基础属性
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            // 2. 处理特殊逻辑
            // 比如说对用户名隐藏后两位为**
            vo.setUsername(vo.getUsername().substring(0, vo.getUsername().length() - 2) + "**");
            // ...
            return vo;
        });
    }

    /**
     * 据id用户查询的接口，查询用户的同时返回用户收货地址列表
     * @param id
     * @return
     */
    @Override
    public UserVO queryUserAndAddressById(Long id) {
        // 1. 查询用户
        User user = getById(id);
//        if (user == null || user.getStatus() == 2) {
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户状态异常!");
        }

        // 2. 查询地址 使用Db静态工具类 防止出现循环依赖(注入其他Service，如AddressService)
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id).list();

        // 3. 封装VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (CollUtil.isNotEmpty(addresses)) {
            userVO.setAddress(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        return userVO;
    }

    /**
     * 根据id批量查询用户，并查询出用户对应的所有地址
     * @param ids
     * @return
     */
    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        // 1. 查询用户
        List<User> users = listByIds(ids);
        if (CollUtil.isEmpty(users)) {
            // List<Object> objects = Collections.emptyList();
            return Collections.emptyList(); // 返回空集合
        }

        // 2. 查询地址
        // 2.1 获取用户id集合
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        // 2.2 根据用户id查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds)
                .list();
        // 2.3 转换地址VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        // 2.4 梳理地址集合，分类整理，相同用户的地址放在同一个集合中
        // 键为用户id list为该用户的地址集合
        Map<Long, List<AddressVO>> addressMap = new LinkedHashMap<>(0);
        if (CollUtil.isNotEmpty(addressVOList)) { // 如果不为空
            // 根据ID分组
             addressMap = addressVOList.stream()
                    .collect(groupingBy(AddressVO::getUserId));
        }

        // 3. 转换VO返回
        List<UserVO> list = new ArrayList<>(users.size());
        for (User user : users) {
            // 3.1 转换user的PO为VO
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            list.add(userVO);
            // 3.2 转换地址VO
            userVO.setAddress(addressMap.get(user.getId()));
        }
        return list;
    }
}
