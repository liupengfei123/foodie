package com.lpf.service.platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lpf.enums.Sex;
import com.lpf.mapper.UsersMapper;
import com.lpf.pojo.Users;
import com.lpf.pojo.bo.UserBO;
import com.lpf.service.platform.UserService;
import com.lpf.utils.DateUtil;
import com.lpf.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liupf
 * @date 2021-03-20 12:22
 */
@Service
public class UserServiceImpl implements UserService {
    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;

    @Override
    public boolean queryUsernameIsExist(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);

        Users users = usersMapper.selectOne(wrapper);

        return users != null;
    }

    @Override
    public Users createUser(UserBO userBO) {
        Users user = new Users();
        user.setId(sid.nextShort());
        user.setUsername(userBO.getUsername());

        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        // 默认头像
        user.setFace(USER_FACE);
        // 默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        // 默认性别为 保密
        user.setSex(Sex.secret.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        if ("111".equals(userBO.getUsername())) {
            throw new RuntimeException("111");
        }

        return user;
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        wrapper.eq("password", password);

        Users users = usersMapper.selectOne(wrapper);

        return users;
    }


}
