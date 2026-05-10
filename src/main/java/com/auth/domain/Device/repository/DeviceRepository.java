package com.auth.domain.Device.repository;

import com.auth.domain.Device.entity.Device;
import com.auth.domain.Users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

    Optional<Device> findByDeviceName(String deviceName);

    boolean existsByUserAndUserAgent(User user, String userAgent);

    List<Device> findAllByUserId(String userId);

    List<Device> findAllByUser_Id(String userId);
}
