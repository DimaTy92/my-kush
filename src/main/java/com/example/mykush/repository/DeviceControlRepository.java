package com.example.mykush.repository;

import com.example.mykush.entity.DeviceControlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceControlRepository extends JpaRepository<DeviceControlEntity, Long> {
    Optional<DeviceControlEntity> getByUrl(String url);
    List<DeviceControlEntity> findAll();
    Optional<DeviceControlEntity> findDeviceControlEntityByUrl(String url);

    List<DeviceControlEntity>findAllByIsChangedPriceTrue();

}
