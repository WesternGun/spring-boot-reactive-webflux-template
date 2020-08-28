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

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class PromotionTargetFileHandler {
    public static final String uploadingDir = String.join(File.separator,
            System.getProperty("user.dir"), "targets_csv");
    private final File destDir = new File(uploadingDir);

    public Mono<String> saveParts(FilePart file) {
        return Mono.just(file)
                .zipWith(createFileDestination(file))
                .flatMap(tuple -> writeFilePart(tuple.getT2(), tuple.getT1()));

    }

    public Mono<Path> createFileDestination(FilePart filePart) {
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
                uploadingDir, filePart.filename()
        );
        File saveFile = new File(saveLocation);
        if (saveFile.exists()) saveFile.delete();
        try {
            saveFile.createNewFile();   // <--------------- here IOException
            return Mono.just(saveFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Mono<String> writeFilePart(Path path, FilePart filePart) {
        if (log.isTraceEnabled()) {
            filePart.content().map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                return new String(bytes, StandardCharsets.UTF_8);
            })
            .doOnNext(content -> log.trace("File name: {}, content: {}", filePart.filename(), content))
            .subscribeOn(Schedulers.immediate())
            .subscribe();
        }
        filePart.transferTo(path);
        log.trace("File part saved to: " + path.getFileName());
        return Mono.just(path.toAbsolutePath().toString());
    }

}
