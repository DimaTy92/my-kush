package com.example.mykush.service;

import com.example.mykush.entity.DeviceControlEntity;
import com.example.mykush.entity.UserEntity;
import com.example.mykush.entity.dto.DeviceControlDTO;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public interface SubscriptionService {
    DeviceControlDTO createSubscription(User user, String url, String chatId);
    List<DeviceControlDTO> lookSubscription(String user);

    UserEntity unsubscribe(User user);

}
