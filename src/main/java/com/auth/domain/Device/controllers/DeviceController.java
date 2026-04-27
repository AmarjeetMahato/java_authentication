package com.auth.domain.Device.controllers;


import com.auth.domain.Device.service.IDeviceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {

    private IDeviceService service;
}
