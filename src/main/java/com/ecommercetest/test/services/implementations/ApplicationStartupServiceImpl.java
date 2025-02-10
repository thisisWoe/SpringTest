package com.ecommercetest.test.services.implementations;

import com.ecommercetest.test.services.ApplicationStartupService;
import com.ecommercetest.test.services.ProviderService;
import com.ecommercetest.test.services.RoleService;
import com.ecommercetest.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Service
public class ApplicationStartupServiceImpl implements ApplicationStartupService {
    @Autowired
    ProviderService providerService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

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
}
