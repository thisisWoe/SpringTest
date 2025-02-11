package com.ecommercetest.test.services;

import com.ecommercetest.test.controllers.dto.device.DeviceAssignedResponse;
import com.ecommercetest.test.domain.device.Device;
import com.ecommercetest.test.domain.device.DeviceState;
import com.ecommercetest.test.domain.device.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeviceService {
    void saveDevices(List<Device> devices);

    DeviceAssignedResponse assignDevice(Long deviceId, Long userId);

    List<DeviceAssignedResponse> getAllDevices();

    Page<DeviceAssignedResponse> getAllDevicesPageable(Pageable pageable, String model, DeviceState state, DeviceType type);

    //  Page<DeviceAssignedResponse> getAllDevicesPageable(Pageable pageable);
}
