package com.example.mykush.mapper;

import com.example.mykush.entity.DeviceControlEntity;
import com.example.mykush.entity.PriceDeviceEntity;
import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.entity.dto.PriceDeviceDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceControlMapper {

    public DeviceControlEntity map(DeviceControlDTO source) {
        DeviceControlEntity deviceControlEntity = new DeviceControlEntity();
        deviceControlEntity.setModel(source.getModel());
        deviceControlEntity.setLastCheckDate(source.getLastCheckDate());
        deviceControlEntity.setPriceUA(source.getPriceUA());
        deviceControlEntity.setPriceUSA(source.getPriceUSA());
        deviceControlEntity.setUrl(source.getUrl());
        return deviceControlEntity;
    }

    public DeviceControlDTO mapToDeviceDTO(DeviceControlEntity source) {
        DeviceControlDTO deviceControlDTO = new DeviceControlDTO();
        deviceControlDTO.setModel(source.getModel());
        deviceControlDTO.setLastCheckDate(source.getLastCheckDate());
        deviceControlDTO.setPriceUA(source.getPriceUA());
        deviceControlDTO.setPriceUSA(source.getPriceUSA());
        deviceControlDTO.setUrl(source.getUrl());
        deviceControlDTO.setPriceDevicesList(mapToListPriceDeviceDTO(source.getPriceDevicesList()));
        return deviceControlDTO;
    }

    public List<DeviceControlDTO> mapToDto(List<DeviceControlEntity> entityList) {
        return entityList.stream()
                .map(this::mapToDeviceDTO)
                .collect(Collectors.toList());
    }

    public PriceDeviceDTO mapToPriceDeviceDTO(PriceDeviceEntity source) {
        return PriceDeviceDTO.builder()
                .priceUA(source.getPriceUA())
                .priceUSA(source.getPriceUSA())
                .localDate(source.getLocalDate())
                .build();
    }

    public List<PriceDeviceDTO> mapToListPriceDeviceDTO(List<PriceDeviceEntity> priceDeviceEntities) {
        return priceDeviceEntities.stream()
                .map(this::mapToPriceDeviceDTO)
                .collect(Collectors.toList());
    }
}
