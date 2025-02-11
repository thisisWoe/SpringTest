package com.ecommercetest.test.controllers.dto.device;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceAssignRequest {

    private final Long deviceId;

    private final Long userId;


    public DeviceAssignRequest(Long deviceId, Long userId) {
        this.deviceId = deviceId;
        this.userId = userId;
    }
}
