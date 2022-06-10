package com.example.mykush.repository;

import com.example.mykush.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getByUserName(String userName);

    @Query("select a from UserEntity a join fetch a.devices where a.userName = :userName")
    Optional<UserEntity> getUserDevicesByUserName(@Param("userName") String userName);

    @Query("select distinct a from UserEntity as a join fetch a.devices as b1 where b1.isChangedPrice = true")
//    @Query("select distinct a from UserEntity as a join fetch a.devices b join fetch b.priceDevicesList c where c.deviceControlEntity.isChangedPrice = true")
    Optional<List<UserEntity>> getUserDevicesWithUpdatedPricesDevice();
}
