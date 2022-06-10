package com.example.mykush.service;

import com.example.mykush.entity.UserEntity;
import com.example.mykush.entity.dto.CreateSubscriptionDTO;
import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.entity.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(CreateSubscriptionDTO user);

    UserEntity getUserByUserName(CreateSubscriptionDTO user);

    List<DeviceControlDTO> getDeviceControlEntity(String userName);

    UserEntity removeDevice(String userName);

    List<UserEntity> findAllUsers();

    List<UserDTO> findUsersWithUpdatedPricesDevice();

}
