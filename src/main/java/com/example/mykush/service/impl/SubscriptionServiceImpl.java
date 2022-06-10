package com.example.mykush.service.impl;

import com.example.mykush.entity.DeviceControlEntity;
import com.example.mykush.entity.UserEntity;
import com.example.mykush.entity.dto.CreateSubscriptionDTO;
import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.entity.dto.UserDTO;
import com.example.mykush.service.SubscriptionService;
import com.example.mykush.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserService userService;

    @Override
    public DeviceControlDTO createSubscription(User user, String url, String chatId) {
        UserDTO userDTO = userService.saveUser(new CreateSubscriptionDTO(url, user, chatId));
        return userDTO.getDevices().stream()
                .filter(device -> Objects.equals(device.getUrl(), url))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Device not saved"));
    }

    @Override
    public List<DeviceControlDTO> lookSubscription(String user) {
        return userService.getDeviceControlEntity(user);
    }

    @Override
    public UserEntity unsubscribe(User user) {
        return userService.removeDevice(user.getUserName());
    }
}

