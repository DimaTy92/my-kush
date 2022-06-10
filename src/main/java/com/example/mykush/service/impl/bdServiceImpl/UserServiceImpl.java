package com.example.mykush.service.impl.bdServiceImpl;

import com.example.mykush.entity.DeviceControlEntity;
import com.example.mykush.entity.PriceDeviceEntity;
import com.example.mykush.entity.UserEntity;
import com.example.mykush.entity.dto.CreateSubscriptionDTO;
import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.entity.dto.UserDTO;
import com.example.mykush.mapper.DeviceControlMapper;
import com.example.mykush.mapper.UserMapper;
import com.example.mykush.repository.UserRepository;
import com.example.mykush.service.DeviceControlService;
import com.example.mykush.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final DeviceControlMapper deviceMapper;
    private final UserRepository userRepository;
    private final DeviceControlService deviceControlService;

    @Override
    @Transactional
    public UserDTO saveUser(CreateSubscriptionDTO user) {
        DeviceControlEntity deviceControlEntity = deviceControlService.getDeviceInfo(user.getUrl());

        List<PriceDeviceEntity> priceDevicesEntityList = deviceControlEntity.getPriceDevicesList();
        if (Objects.isNull(deviceControlEntity.getPriceDevicesList())) {
            priceDevicesEntityList = new ArrayList<>();

            PriceDeviceEntity priceDeviceEntity = PriceDeviceEntity.builder()
                    .priceUA(deviceControlEntity.getPriceUA())
                    .priceUSA(deviceControlEntity.getPriceUSA())
                    .localDate(deviceControlEntity.getLastCheckDate())
                    .deviceControlEntity(deviceControlEntity)
                    .build();

            priceDevicesEntityList.add(priceDeviceEntity);
        }

        deviceControlEntity.setPriceDevicesList(priceDevicesEntityList);
        deviceControlEntity.setIsChangedPrice(false);

        UserEntity userEntity = userRepository.getByUserName(user.getTelegramUser().getUserName())
                .orElseGet(() -> userMapper.mapUser(user.getTelegramUser(), user.getChatId()));
        addDevice(userEntity, deviceControlEntity);
        UserEntity saved = userRepository.save(userEntity);
        return userMapper.mapUserDto(saved);
    }

    @Override
    public UserEntity getUserByUserName(CreateSubscriptionDTO user) {
        return userRepository.getByUserName(user.getTelegramUser().getUserName()).orElseThrow();
    }

    @Override
    @Transactional
    public List<DeviceControlDTO> getDeviceControlEntity(String userName) {
        return userRepository.getUserDevicesByUserName(userName)
                .map(UserEntity::getDevices)
                .map(deviceMapper::mapToDto)
                .orElse(Collections.emptyList());
    }

    @Override
    public UserEntity removeDevice(String userName) {
        UserEntity userEntity = userRepository.getByUserName(userName).orElseThrow();
        userEntity.setDevices(null);
        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> findUsersWithUpdatedPricesDevice() {
        return userRepository.getUserDevicesWithUpdatedPricesDevice()
                .map(userMapper::mapToDto)
                .orElse(Collections.emptyList());
    }

    private void addDevice(UserEntity user, DeviceControlEntity device) {
        if (Objects.nonNull(user.getDevices())) {
            if (!contains(user, device)) {
                user.getDevices().add(device);
            }
        } else {
            user.setDevices(List.of(device));
        }
    }

    private boolean contains(UserEntity user, DeviceControlEntity deviceControlEntity) {
        return user.getDevices().stream()
                .anyMatch(device -> device.getId().equals(deviceControlEntity.getId()));
    }
}
