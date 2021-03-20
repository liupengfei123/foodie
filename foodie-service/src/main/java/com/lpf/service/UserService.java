package com.lpf.service;

import com.lpf.pojo.Users;
import com.lpf.pojo.bo.UserBO;

public interface UserService {

    boolean queryUsernameIsExist(String username);

    Users createUser(UserBO userBO);

    Users queryUserForLogin(String username, String password);


}
