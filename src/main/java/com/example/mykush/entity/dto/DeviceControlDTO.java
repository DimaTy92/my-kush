package com.example.mykush.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceControlDTO {

    private String model;
    private String priceUA;
    private String priceUSA;
    private String url;
    private LocalDate lastCheckDate;
    private Boolean isChangedPrice;

    private List<PriceDeviceDTO> priceDevicesList;


    @Override
    public String toString() {
        return  "\n" + model +", ціна на даний час "+
                priceUA + ", або " +
                priceUSA + "." +"\n";
    }
}
