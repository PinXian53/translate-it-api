package com.pino.translateitapi.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class FileSystemUtils {

    private FileSystemUtils() {}

    public static void safeDeleteFile(File file) {
        try {
            Files.delete(file.toPath());
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
        }
    }

}
