package com.example.mykush.service.impl;

import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.entity.dto.UserDTO;
import com.example.mykush.service.DeviceControlService;
import com.example.mykush.service.SubscriptionService;
import com.example.mykush.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class TelegramBotService {

    private final SubscriptionService subscriptionService;
    private final DeviceControlService deviceControlService;
    private final UserService userService;

    public DeviceControlDTO createSubscription(String url, User user, String chatId) {
        return subscriptionService.createSubscription(user, url, chatId);
    }

    public List<DeviceControlDTO> lookSubscription(String userName) {
        return subscriptionService.lookSubscription(userName);
    }

    public void unsubscribe(User user) {
        subscriptionService.unsubscribe(user);
    }

    public List<UserDTO> createDistribution() {
        List<UserDTO> usersWithUpdatedPricesDevice = userService.findUsersWithUpdatedPricesDevice();
        deviceControlService.resetIsChangedPriceForDevices();

        return usersWithUpdatedPricesDevice;
    }
}
