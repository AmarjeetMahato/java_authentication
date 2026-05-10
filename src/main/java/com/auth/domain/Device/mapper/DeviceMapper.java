package com.auth.domain.Device.mapper;

import com.auth.domain.Device.dto.DeviceDto;
import com.auth.domain.Device.dto.DeviceResponseDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Users.entity.User;
import com.auth.globalExceptions.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeviceMapper {

    // DTO → ENTITY
    public Device toEntity(DeviceDto dto, User user) {

        if (dto == null) {
            throw new IllegalArgumentException("DeviceDto must not be null");
        }

        return Device.builder()
                .user(user)
                .deviceName(dto.getDeviceName())
                .deviceType(dto.getDeviceType())
                .os(dto.getOs())
                .browser(dto.getBrowser())
                .ipAddress(dto.getIpAddress())
                .userAgent(dto.getUserAgent())
                .isActive(true)
                .isTrusted(false)
                .isBlocked(false)
                .lastLoginAt(LocalDateTime.now())
                .build();
    }

//    // ENTITY → RESPONSE DTO
    /**
     * ENTITY -> RESPONSE DTO
     */
    public DeviceResponseDto toResponse(Device device) {

        if (device == null) {
            throw new BadRequestException(
                    "Device must not be null"
            );
        }

        return DeviceResponseDto.builder()
                .id(device.getId())

                // ✅ FIXED
                .userId(
                        device.getUser() != null
                                ? device.getUser().getId()
                                : null
                )
                .deviceId(device.getId())
                .deviceName(device.getDeviceName())
                .deviceType(device.getDeviceType())
                .os(device.getOs())
                .browser(device.getBrowser())
                .ipAddress(device.getIpAddress())
                .userAgent(device.getUserAgent())
                .isActive(device.getIsActive())
                .isTrusted(device.getIsTrusted())
                .isBlocked(device.getIsBlocked())
                .lastLoginAt(device.getLastLoginAt())
                .createdAt(device.getCreatedAt())
                .message("Device fetched successfully")
                .build();
    }

    // UPDATE EXISTING DEVICE (LOGIN AGAIN)
    public void updateEntity(Device existing,DeviceDto dto) {
        if (existing == null) {
            throw new IllegalArgumentException("Existing device must not be null");
        }

        if (dto == null) {
            throw new IllegalArgumentException("CreateDeviceDto must not be null");
        }

        existing.setDeviceName(dto.getDeviceName());
        existing.setDeviceType(dto.getDeviceType());
        existing.setOs(dto.getOs());
        existing.setBrowser(dto.getBrowser());
        existing.setIpAddress(dto.getIpAddress());
        existing.setUserAgent(dto.getUserAgent());
        existing.setLastLoginAt(LocalDateTime.now());
        existing.setIsActive(true);
    }
}