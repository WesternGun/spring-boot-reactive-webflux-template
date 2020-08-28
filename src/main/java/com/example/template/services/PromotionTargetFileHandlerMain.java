package com.example.template.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class PromotionTargetFileHandlerMain {
    public static final String uploadingDir = String.join(File.separator,
            System.getProperty("user.dir"), "targets_csv");
    private static final File destDir = new File(uploadingDir);


    public static void main(String[] args) {
        createFileDestination("target.csv");
    }

    public static void createFileDestination(String filename) {
        if (!destDir.exists()) {
            try {
                Files.createDirectory(Path.of(uploadingDir));
            } catch (FileAlreadyExistsException e) {
                log.trace("File dir already exists: {}", uploadingDir);
            } catch (IOException e) {
                log.error("Cannot create temp file dir {}", uploadingDir, e);
                throw new RuntimeException(e);
            }
        }
        String saveLocation = String.join(File.separator,
                uploadingDir, filename
        );
        File saveFile = new File(saveLocation);
        if (saveFile.exists()) saveFile.delete();
        try {
            saveFile.createNewFile();   // <--------------- here IOException
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
