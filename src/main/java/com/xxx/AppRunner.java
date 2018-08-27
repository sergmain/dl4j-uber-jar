package com.xxx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@Slf4j
public class AppRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            SpringApplication.run(AppRunner.class, args);
        }
        finally {
            log.info("{==== List of system properties. Start ======");
            Properties properties = System.getProperties();
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                log.info("{} : {}", entry.getKey(), entry.getValue());
            }
            log.info("{==== List of system properties. End ======");
            dir("target/.javacpp", 0);
        }
    }

    private static void dir(String parent, int level) throws IOException, InterruptedException {
        File dirName = new File(parent);
        String fileNames[] = dirName.list();
        if (fileNames==null) {
            return;
        }

        for (String fileName : fileNames) {
            File fullPath = new File(parent + File.separatorChar + fileName);

            if (fullPath.isFile()) {
                log.info("{}{}", StringUtils.repeat("  ", level), fullPath.getPath());
                String console = execCommand(fullPath);
                LineIterator it = IOUtils.lineIterator(new StringReader(console));
                while (it.hasNext()) {
                    log.info("{}{}", StringUtils.repeat("  ", level+2), (it.nextLine()));
                }
            }
            else {
                log.info("{}{}:", StringUtils.repeat("  ", level), fullPath.getPath());
                dir(parent + File.separatorChar + fileName, level + 1);
            }
        }
    }

    public static String execCommand(File f) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add("ldd");
//        cmd.add("-v");
        cmd.add(f.getPath());

        ProcessBuilder pb = new ProcessBuilder();
        pb.command(cmd);
        pb.directory(new File("."));
        pb.redirectErrorStream(true);
        final Process process = pb.start();

        final StringBuilder out = new StringBuilder();
        final Thread reader = new Thread(() -> {
            try {
                final InputStream is = process.getInputStream();
                int c;
                while ((c = is.read()) != -1) {
                    out.append((char) c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        reader.start();

        final int exitCode = process.waitFor();
        reader.join();

        final String console = out.toString();
        return console;
    }

}

