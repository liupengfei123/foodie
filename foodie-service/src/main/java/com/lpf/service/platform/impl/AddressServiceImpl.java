package com.lpf.service.platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lpf.enums.YesOrNo;
import com.lpf.mapper.UserAddressMapper;
import com.lpf.pojo.UserAddress;
import com.lpf.pojo.bo.AddressBO;
import com.lpf.service.platform.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress ua = new UserAddress();
        ua.setUserId(userId);
        return userAddressMapper.selectList(new QueryWrapper<>(ua));
    }

    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，如果没有，则新增为‘默认地址’
        Integer isDefault = 0;

        UserAddress ua = new UserAddress();
        ua.setUserId(addressBO.getUserId());
        UserAddress address = userAddressMapper.selectOne(new QueryWrapper<>(ua));
        if (address == null) {
            isDefault = 1;
        }

        String addressId = sid.nextShort();

        // 2. 保存地址到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress);

        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(newAddress);
    }

    @Override
    public void updateUserAddress(AddressBO addressBO) {
        UserAddress ua = new UserAddress();
        ua.setUserId(addressBO.getAddressId());

        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, pendingAddress);
        pendingAddress.setUpdatedTime(new Date());

        userAddressMapper.update(pendingAddress, new QueryWrapper<>(ua));
    }

    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setId(addressId);
        address.setUserId(userId);

        userAddressMapper.delete(new QueryWrapper<>(address));
    }

    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        // 1. 查找默认地址，设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> list  = userAddressMapper.selectList(new QueryWrapper<>(queryAddress));
        for (UserAddress ua : list) {
            ua.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateById(ua);
        }

        // 2. 根据地址id修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateById(defaultAddress);
    }

    @Override
    public UserAddress queryUserAddres(String userId, String addressId) {
        UserAddress singleAddress = new UserAddress();
        singleAddress.setId(addressId);
        singleAddress.setUserId(userId);

        return userAddressMapper.selectOne(new QueryWrapper<>(singleAddress));
    }
}
