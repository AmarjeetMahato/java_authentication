package com.auth.domain.Device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceDto {
    @NotBlank(message = "User ID must not be empty")
    private String userId;

    @NotBlank(message = "Device name is required (e.g. Chrome on Windows)")
    @Size(max = 100, message = "Device name must not exceed 100 characters")
    private String deviceName;

    @NotBlank(message = "Device type is required")
    @Pattern(
            regexp = "MOBILE|DESKTOP|TABLET",
            message = "Device type must be one of: MOBILE, DESKTOP, TABLET"
    )
    private String deviceType;

    @NotBlank(message = "Operating system is required")
    private String os;

    @Size(max = 100, message = "Browser name must not exceed 100 characters")
    private String browser;

    @NotBlank(message = "IP address is required")
    @Pattern(
            regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$",
            message = "Invalid IP address format"
    )
    private String ipAddress;

    @Size(max = 1000, message = "User agent must not exceed 1000 characters")
    private String userAgent;

}
