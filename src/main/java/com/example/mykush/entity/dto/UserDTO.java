package com.example.mykush.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String chatId;

    private List<DeviceControlDTO> devices;

}

