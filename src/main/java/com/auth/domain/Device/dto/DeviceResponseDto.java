package com.auth.domain.Device.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDto {

    private String id;
    private String userId;

    private String deviceName;
    private String deviceType;
    private String os;
    private String browser;
    private String ipAddress;

    private Boolean isActive;
    private Boolean isTrusted;
    private Boolean isBlocked;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
}