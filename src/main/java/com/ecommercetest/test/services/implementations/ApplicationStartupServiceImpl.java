package com.ecommercetest.test.services.implementations;

import com.ecommercetest.test.domain.device.Device;
import com.ecommercetest.test.domain.device.DeviceState;
import com.ecommercetest.test.domain.device.DeviceType;
import com.ecommercetest.test.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationStartupServiceImpl implements ApplicationStartupService {
    private final
    ProviderService providerService;

    private final
    RoleService roleService;

    private final
    UserService userService;

    private final DeviceService deviceService;

    public ApplicationStartupServiceImpl(ProviderService providerService, RoleService roleService, UserService userService, DeviceService deviceService) {
        this.providerService = providerService;
        this.roleService = roleService;
        this.userService = userService;
        this.deviceService = deviceService;
    }

    @Override
    @Transactional
    public void startApplication() {
        try {
            providerService.checkProvidersPresentOrSave();
            roleService.checkRolesPresentOrSave();
            userService.checkAdminPresentOrSave();
        } catch (Exception e) {
            System.out.println("Something went wrong while trying the application startup\n: " + e);
        }
    }

    @Override
    public void writeProjectStructure() throws IOException {
        Path rootPath = Paths.get("").toAbsolutePath();
        Path outputFile = Paths.get("project_structure.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

                private String getIndentation(Path path) {
                    int depth = rootPath.relativize(path).getNameCount();
                    return "    ".repeat(Math.max(0, depth));
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    String dirName = dir.getFileName().toString();
                    if (dirName.startsWith(".") || dirName.equals("target") || dirName.equals("build")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    writer.write(getIndentation(dir) + "└── " + dir.getFileName());
                    writer.newLine();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    writer.write(getIndentation(file) + "    " + file.getFileName());
                    writer.newLine();
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    @Override
    public void createDevices() {
        List<Device> devices = new ArrayList<>();

        Device iphone12 = Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("iPhone 12")
                .build();
        devices.add(iphone12);

        Device samsungTablet = Device.builder()
                .type(DeviceType.TABLET)
                .state(DeviceState.AVAILABLE)
                .model("Samsung Galaxy Tablet")
                .build();
        devices.add(samsungTablet);

        Device lenovoLaptop = Device.builder()
                .type(DeviceType.LAPTOP)
                .state(DeviceState.AVAILABLE)
                .model("Lenovo ThinkPad X1 Carbon")
                .build();
        devices.add(lenovoLaptop);

        devices.add(Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("Google Pixel 6")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("OnePlus 9 Pro")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.TABLET)
                .state(DeviceState.AVAILABLE)
                .model("iPad Pro")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.LAPTOP)
                .state(DeviceState.AVAILABLE)
                .model("Dell XPS 13")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.LAPTOP)
                .state(DeviceState.AVAILABLE)
                .model("HP Spectre x360")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("Samsung Galaxy S21")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("iPhone SE")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.TABLET)
                .state(DeviceState.AVAILABLE)
                .model("Microsoft Surface Pro 7")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.LAPTOP)
                .state(DeviceState.AVAILABLE)
                .model("Apple MacBook Pro 16")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.TABLET)
                .state(DeviceState.AVAILABLE)
                .model("Lenovo Tab P11 Pro")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("Sony Xperia 1 III")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.LAPTOP)
                .state(DeviceState.AVAILABLE)
                .model("Asus ZenBook 14")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.TABLET)
                .state(DeviceState.AVAILABLE)
                .model("Huawei MatePad Pro")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("Motorola Edge Plus")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.LAPTOP)
                .state(DeviceState.AVAILABLE)
                .model("Acer Swift 3")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.TABLET)
                .state(DeviceState.AVAILABLE)
                .model("Amazon Fire HD 10")
                .build());

        devices.add(Device.builder()
                .type(DeviceType.SMARTPHONE)
                .state(DeviceState.AVAILABLE)
                .model("Nokia 8.3 5G")
                .build());

        deviceService.saveDevices(devices);
    }
}
