package com.example.mykush.service.impl;

import com.example.mykush.bot.JavaTelegramBotApplication;
import com.example.mykush.service.DeviceControlService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Scheduler {

    private final DeviceControlService deviceControlService;
    private final JavaTelegramBotApplication javaTelegramBotApplication;

    @Scheduled(cron = "0 0 10 * * ?")
    private void updateDeviceInfoByScheduler() {
        deviceControlService.updateDeviceInfo();
        javaTelegramBotApplication.createDistribution();
    }
}
