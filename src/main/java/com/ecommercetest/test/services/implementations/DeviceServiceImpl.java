package com.ecommercetest.test.services.implementations;

import com.ecommercetest.test.controllers.dto.device.DeviceAssignedResponse;
import com.ecommercetest.test.controllers.response.UserMinimized;
import com.ecommercetest.test.domain.device.Device;
import com.ecommercetest.test.domain.device.DeviceState;
import com.ecommercetest.test.domain.device.DeviceType;
import com.ecommercetest.test.domain.user.User;
import com.ecommercetest.test.repositories.DeviceRepository;
import com.ecommercetest.test.repositories.UserRepository;
import com.ecommercetest.test.services.DeviceService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;


    private final UserRepository userRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveDevices(List<Device> devices) {
        deviceRepository.saveAll(devices);
    }

    @Override
    public DeviceAssignedResponse assignDevice(Long deviceId, Long userId) {
        Device deviceFound = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found."));
        boolean deviceStateNotAvailable = deviceFound.getState() == DeviceState.ASSIGNED ||
                deviceFound.getState() == DeviceState.DISMISSED ||
                deviceFound.getState() == DeviceState.MAINTENANCE;
        if (deviceStateNotAvailable) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Device is not available.");
        }
        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        deviceFound.setUser(userFound);
        deviceRepository.save(deviceFound);
        return new DeviceAssignedResponse(
                deviceFound,
                UserMinimized.builder()
                        .id(userFound.getId())
                        .fullName(userFound.getFullName())
                        .username(userFound.getUsername())
                        .email(userFound.getEmail())
                        .provider(userFound.getProvider())
                        .build()
        );
    }

    @Override
    public List<DeviceAssignedResponse> getAllDevices() {
        return deviceRepository.findAll().stream().map(device -> {

            Optional<User> userFound = device.getUser() != null
                    ? userRepository.findById(device.getUser().getId())
                    : Optional.empty();
            User user = userFound.orElse(null);

            return new DeviceAssignedResponse(
                    device,
                    user == null ? null :
                            UserMinimized.builder()
                                    .id(user.getId())
                                    .fullName(user.getFullName())
                                    .username(user.getUsername())
                                    .email(user.getEmail())
                                    .provider(user.getProvider())
                                    .build());
        }).toList();
    }

    @Override
    public Page<DeviceAssignedResponse> getAllDevicesPageable(Pageable pageable, String model, @Nullable DeviceState state, @Nullable DeviceType type) {
        Page<Device> devicePage = deviceRepository.findDevicesByFilters(pageable, model, state != null ? state.ordinal() : null, type != null ? type.ordinal() : null);

        return devicePage.map(device -> {
            User user = device.getUser();
            return new DeviceAssignedResponse(
                    device,
                    user == null ? null : UserMinimized.builder()
                            .id(user.getId())
                            .fullName(user.getFullName())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .provider(user.getProvider())
                            .build()
            );
        });
    }

//    @Override
//    public Page<DeviceAssignedResponse> getAllDevicesPageable(Pageable pageable) {
//        Page<Device> devicePage = deviceRepository.findAll(pageable);
//
//        return devicePage.map(device -> {
//            User user = device.getUser();
//            return new DeviceAssignedResponse(
//                    device,
//                    user == null ? null : UserMinimized.builder()
//                            .id(user.getId())
//                            .fullName(user.getFullName())
//                            .username(user.getUsername())
//                            .email(user.getEmail())
//                            .provider(user.getProvider())
//                            .build()
//            );
//        });
//    }
}
