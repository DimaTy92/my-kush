package com.example.mykush.service.impl;

import com.example.mykush.ParserJabko;
import com.example.mykush.entity.DeviceControlEntity;
import com.example.mykush.entity.PriceDeviceEntity;
import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.mapper.DeviceControlMapper;
import com.example.mykush.repository.DeviceControlRepository;
import com.example.mykush.service.DeviceControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DeviceControlServiceImpl implements DeviceControlService {

    private final ParserJabko parserJabko;
    private final DeviceControlRepository repository;
    private final DeviceControlMapper deviceControlMapper;

    @Override
    public DeviceControlEntity getDeviceInfo(final String url) {
        return repository.getByUrl(url).orElseGet(() -> createDeviceInfo(url));
    }

    @Override
    public List<DeviceControlEntity> findAllDevices() {
        return repository.findAll();
    }

    @Override
    public List<DeviceControlEntity> updateDeviceInfo() {
        List<DeviceControlEntity> allDevices = findAllDevices();
        List<DeviceControlEntity> updatedDevices = allDevices.stream().map(this::getUpdateForDevices).collect(Collectors.toList());
        return repository.saveAll(updatedDevices);
    }

    @Override
    public void resetIsChangedPriceForDevices() {
        List<DeviceControlEntity> devices = repository.findAllByIsChangedPriceTrue();
        devices.forEach(device ->device.setIsChangedPrice(false));
    }

    private DeviceControlEntity getUpdateForDevices(DeviceControlEntity entity) {
        DeviceControlDTO deviceInfo = parserJabko.getDeviceInfo(entity.getUrl());
        if(!entity.getPriceUA().equals(deviceInfo.getPriceUA()) || !entity.getPriceUSA().equals(deviceInfo.getPriceUSA())) {
            entity.setPriceUA(deviceInfo.getPriceUA());
            entity.setPriceUSA(deviceInfo.getPriceUSA());
            entity.setLastCheckDate(deviceInfo.getLastCheckDate());
            entity.setIsChangedPrice(true);

            List<PriceDeviceEntity> priceDevicesEntityList = entity.getPriceDevicesList();
            priceDevicesEntityList.add(PriceDeviceEntity.builder()
                            .priceUA(deviceInfo.getPriceUA())
                            .priceUSA(deviceInfo.getPriceUSA())
                            .localDate(deviceInfo.getLastCheckDate())
                            .deviceControlEntity(entity)
                    .build());

            entity.setPriceDevicesList(priceDevicesEntityList);
        }
        return entity;
    }

    private DeviceControlEntity createDeviceInfo(String url) {
        DeviceControlDTO deviceInfo = parserJabko.getDeviceInfo(url);
        DeviceControlEntity entityToSave = deviceControlMapper.map(deviceInfo);
        return repository.save(entityToSave);
    }
}
