package com.auth.domain.Device.service;

import com.auth.domain.Device.dto.DeviceDto;
import com.auth.domain.Device.dto.DeviceResponseDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Device.mapper.DeviceMapper;
import com.auth.domain.Device.repository.DeviceRepository;
import com.auth.domain.Users.entity.User;
import com.auth.globalExceptions.BadRequestException;
import com.auth.globalExceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements  IDeviceService {

     private final DeviceRepository deviceRepository;
     private  final DeviceMapper deviceMapper;

    @Override
    public DeviceResponseDto registerDevice(DeviceDto dto, User user) {
        if (dto == null) {
            throw new BadRequestException("Device data is required");
        }

        if (user == null) {
            throw new BadRequestException("User is required for device registration");
        }

        // ✅ Check duplicate device (based on userAgent or IP)
        boolean exists = deviceRepository
                .existsByUserAndUserAgent(user, dto.getUserAgent());

        if (exists) {
            throw new BadRequestException("Device already registered for this user");
        }
        // ✅ Convert DTO → Entity
        Device device = deviceMapper.toEntity(dto, user);
        // ✅ Save device
        Device savedDevice = deviceRepository.save(device);
        return  deviceMapper.toResponse(savedDevice);
    }

    @Override
    public DeviceResponseDto getDeviceById(String deviceId) {

           Device device = deviceRepository.findById(deviceId).orElseThrow(
                   ()-> new ResourceNotFoundException("Device not found")
           );

          return  deviceMapper.toResponse(device);
    }

    @Override
    @Transactional()
    public List<DeviceResponseDto> getUserDevices(String userId) {

        if (userId == null || userId.isBlank()) {
            throw new BadRequestException("User ID is required");
        }

        // ✅ Fetch all devices for user
        List<Device> devices = deviceRepository
                .findAllByUserId(userId);

        // ✅ No devices found
        if (devices.isEmpty()) {
            throw new BadRequestException(
                    "No devices found for this user"
            );
        }

        // ✅ Convert Entity -> Response DTO
        return devices.stream()
                .map(deviceMapper::toResponse)
                .toList();
    }

    @Override
    public DeviceResponseDto updateDevice(String deviceId, DeviceDto dto) {
        return null;
    }

    @Override
    @Transactional
    public void logoutDevice(String deviceId) {

        if (deviceId == null || deviceId.isBlank()) {
            throw new BadRequestException("Device ID is required");
        }

        // ✅ Find device
        Device device = deviceRepository
                .findById(deviceId)
                .orElseThrow(() ->
                        new BadRequestException("Device not found"));

        // ✅ Logout device
        device.setIsActive(false);

        // ✅ Remove refresh token
        device.setRefreshToken(null);

        // ✅ Optional session expiry
        device.setExpiresAt(LocalDateTime.now());

        deviceRepository.save(device);
    }


    @Override
    @Transactional
    public void logoutAllDevices(String userId) {

        if (userId == null || userId.isBlank()) {
            throw new BadRequestException("User ID is required");
        }

        // ✅ Fetch user devices
        List<Device> devices = deviceRepository
                .findAllByUserId(userId);

        if (devices.isEmpty()) {
            throw new BadRequestException(
                    "No active devices found"
            );
        }

        // ✅ Logout all devices
        devices.forEach(device -> {
            device.setIsActive(false);
            device.setRefreshToken(null);
            device.setExpiresAt(LocalDateTime.now());
        });

        deviceRepository.saveAll(devices);
    }

    @Override
    @Transactional
    public void blockDevice(String deviceId) {

        if (deviceId == null || deviceId.isBlank()) {
            throw new BadRequestException("Device ID is required");
        }

        // ✅ Find device
        Device device = deviceRepository
                .findById(deviceId)
                .orElseThrow(() ->
                        new BadRequestException("Device not found"));

        // ✅ Block device
        device.setIsBlocked(true);

        // ✅ Force logout
        device.setIsActive(false);

        // ✅ Remove refresh token
        device.setRefreshToken(null);

        deviceRepository.save(device);
    }


    @Override
    @Transactional
    public void trustDevice(String deviceId) {

        if (deviceId == null || deviceId.isBlank()) {
            throw new BadRequestException("Device ID is required");
        }

        // ✅ Find device
        Device device = deviceRepository
                .findById(deviceId)
                .orElseThrow(() ->
                        new BadRequestException("Device not found"));

        // ✅ Cannot trust blocked device
        if (Boolean.TRUE.equals(device.getIsBlocked())) {

            throw new BadRequestException(
                    "Blocked device cannot be trusted"
            );
        }

        // ✅ Trust device
        device.setIsTrusted(true);

        deviceRepository.save(device);
    }
}
