package com.example.mykush.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDeviceDTO {
    private Long id;
    private String priceUA;
    private String priceUSA;
    private LocalDate localDate;
}
