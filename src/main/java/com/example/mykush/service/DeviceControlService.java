package com.example.mykush.service;

import com.example.mykush.entity.DeviceControlEntity;

import java.util.List;

public interface DeviceControlService {
    DeviceControlEntity getDeviceInfo(String url);
    List<DeviceControlEntity> findAllDevices();
    List<DeviceControlEntity> updateDeviceInfo();
    void resetIsChangedPriceForDevices();
}
