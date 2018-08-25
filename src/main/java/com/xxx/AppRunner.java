package com.xxx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@Slf4j
public class AppRunner {
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println(String.format("%s : %s", entry.getKey(), entry.getValue()));
        }
        try {
            SpringApplication.run(AppRunner.class, args);
        }
        finally {
            dir("./target/.javacpp", 0);
        }
    }

    private static void dir(String parent, int level) {
        File dirName = new File(parent);
        String fileNames[] = dirName.list();
        if (fileNames==null) {
            return;
        }

        for (String fileName : fileNames) {
            File fullPath = new File(parent + File.separatorChar + fileName);

            if (fullPath.isFile()) {
                log.info("{}{}", StringUtils.repeat("  ", level), fullPath.getPath());
            }
            else {
                log.info("{}{}:", StringUtils.repeat("  ", level), fullPath.getPath());
                dir(parent + File.separatorChar + fileName, level + 1);
            }
        }
    }
}

