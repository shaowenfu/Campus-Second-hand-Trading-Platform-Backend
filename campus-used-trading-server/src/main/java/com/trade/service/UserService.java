package com.trade.service;

import com.trade.dto.UserLoginDTO;
import com.trade.entity.User;

public interface UserService {
    User wxlogin(UserLoginDTO userLoginDTO);
}
