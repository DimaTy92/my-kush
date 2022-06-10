package com.example.mykush.mapper;

import com.example.mykush.entity.UserEntity;
import com.example.mykush.entity.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final DeviceControlMapper deviceMapper;

    public UserEntity mapUser(User user, String chatId) {
        return UserEntity.builder()
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .chatId(chatId)
                .build();
    }

    public UserDTO mapUserDto(UserEntity source) {
        return UserDTO.builder()
                .setChatId(source.getChatId())
                .setUserName(source.getUserName())
                .setLastName(source.getLastName())
                .setFirstName(source.getFirstName())
                .setDevices(deviceMapper.mapToDto(source.getDevices()))
                .build();
    }

    public List<UserDTO> mapToDto(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(this::mapUserDto)
                .collect(Collectors.toList());
    }
}
