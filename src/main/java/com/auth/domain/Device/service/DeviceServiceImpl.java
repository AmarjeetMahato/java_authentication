package com.auth.domain.Device.service;

import com.auth.domain.Device.dto.DeviceDto;
import com.auth.domain.Device.dto.DeviceResponseDto;
import com.auth.domain.Device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements  IDeviceService {

     private DeviceRepository deviceRepository;


    @Override
    public DeviceResponseDto registerDevice(DeviceDto dto) {


    }

    @Override
    public DeviceResponseDto getDeviceById(String deviceId) {
        return null;
    }

    @Override
    public List<DeviceResponseDto> getUserDevices(String userId) {
        return List.of();
    }

    @Override
    public DeviceResponseDto updateDevice(String deviceId, DeviceDto dto) {
        return null;
    }

    @Override
    public void logoutDevice(String deviceId) {

    }

    @Override
    public void logoutAllDevices(String userId) {

    }

    @Override
    public void blockDevice(String deviceId) {

    }

    @Override
    public void trustDevice(String deviceId) {

    }
}
