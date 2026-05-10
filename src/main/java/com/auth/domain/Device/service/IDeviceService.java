package com.auth.domain.Device.service;

import com.auth.domain.Device.dto.DeviceDto;
import com.auth.domain.Device.dto.DeviceResponseDto;
import com.auth.domain.Users.entity.User;

import java.util.List;

public interface IDeviceService {

    DeviceResponseDto registerDevice(DeviceDto dto, User user);

    DeviceResponseDto getDeviceById(String deviceId);

    List<DeviceResponseDto> getUserDevices(String userId);

    DeviceResponseDto updateDevice(String deviceId,DeviceDto dto);

    void logoutDevice(String deviceId);

    void logoutAllDevices(String userId);

    void blockDevice(String deviceId);

    void trustDevice(String deviceId);
}
