package com.example.mykush.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device_control")
public class DeviceControlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    private String priceUA;
    private String priceUSA;
    private String url;
    private LocalDate lastCheckDate;
    private Boolean isChangedPrice;

    @ManyToMany(mappedBy = "devices")
    private List<UserEntity> users;

    @OneToMany(mappedBy = "deviceControlEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST},fetch = FetchType.EAGER)
    private List<PriceDeviceEntity> priceDevicesList;

    @Override
    public String toString() {
        return  "\n" + model +", ціна на даний час "+
                priceUA + ", або " +
                priceUSA + ".";
    }
}
