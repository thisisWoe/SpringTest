package com.ecommercetest.test.controllers;

import com.ecommercetest.test.controllers.dto.device.DeviceAssignRequest;
import com.ecommercetest.test.controllers.dto.device.DeviceAssignedResponse;
import com.ecommercetest.test.controllers.dto.device.DevicePagedListResponse;
import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.domain.device.DeviceState;
import com.ecommercetest.test.domain.device.DeviceType;
import com.ecommercetest.test.services.DeviceService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("device/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> assignDevice(@RequestBody DeviceAssignRequest request) {
        if (request.getDeviceId() == null || request.getUserId() == null) {
            throw new IllegalArgumentException("Device ID and User ID can't be null.");
        }
        DeviceAssignedResponse response = deviceService.assignDevice(request.getDeviceId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    @GetMapping("device/get-all")
    //    public ResponseEntity<List<DeviceAssignedResponse>> getAllDevices() {
    //        List<DeviceAssignedResponse> response = deviceService.getAllDevices();
    //        return ResponseEntity.status(HttpStatus.OK).body(response);
    //    }

    @GetMapping("device/get-all")
    public ResponseEntity<DevicePagedListResponse> getAllDevices(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) Integer type
    ) {
        DeviceState deviceState = state == null ? null : DeviceState.values()[state];
        DeviceType deviceType = type == null ? null : DeviceType.values()[type];
        DevicePagedListResponse response = new DevicePagedListResponse(deviceService.getAllDevicesPageable(pageable, model, deviceState, deviceType));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
