package com.ecommercetest.test.controllers.dto.device;

import com.ecommercetest.test.controllers.response.ApiResponse;
import com.ecommercetest.test.controllers.response.UserMinimized;
import com.ecommercetest.test.domain.device.Device;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceAssignedResponse implements ApiResponse {
    private final Device device;

    private final UserMinimized user;


    public DeviceAssignedResponse(Device device, UserMinimized user) {
        this.device = device;
        this.user = user;
    }
}
