package com.daxtonl.mediatracker.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

// Runs once at startup and imports every tracker file in mediatracker.import.dir.
// While the app uses in-memory H2, this reloads your data after every restart.
@Component
@ConditionalOnProperty(name = "mediatracker.import.enabled", havingValue = "true")
public class JsonImportRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(JsonImportRunner.class);

    private final JsonTrackerImporter importer;
    private final Path dir;
    private final String skipPrefix;

    public JsonImportRunner(JsonTrackerImporter importer,
                            @Value("${mediatracker.import.dir}") String dir,
                            @Value("${mediatracker.import.skip-prefix:test}") String skipPrefix) {
        this.importer = importer;
        this.dir = Path.of(dir);
        this.skipPrefix = skipPrefix;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        if (!Files.isDirectory(dir)) {
            log.warn("JSON import skipped: {} is not a directory", dir.toAbsolutePath().normalize());
            return;
        }

        List<Path> files;
        try (Stream<Path> stream = Files.list(dir)) {
            files = stream
                    .filter(p -> p.getFileName().toString().endsWith(".json"))
                    .filter(p -> skipPrefix.isEmpty() || !p.getFileName().toString().startsWith(skipPrefix))
                    .sorted()
                    .toList();
        }

        int imported = 0;
        for (Path file : files) {
            try {
                if (importer.importFile(file) == JsonTrackerImporter.Result.IMPORTED) {
                    imported++;
                }
            } catch (RuntimeException e) {
                // One bad file shouldn't stop the others or crash startup.
                log.error("Failed to import {}: {}", file.getFileName(), e.getMessage());
            }
        }
        log.info("JSON import finished: {} of {} file(s) imported", imported, files.size());
    }
}
