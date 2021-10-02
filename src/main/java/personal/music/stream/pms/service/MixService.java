package personal.music.stream.pms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import personal.music.stream.pms.mix.Mix;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.*;

import com.fasterxml.jackson.databind.*;

@Service
public class MixService {

    private Logger log = LoggerFactory.getLogger(MixService.class);

    private static List<String> imageExtensions = List.of(".jpg", ".png", ".webp");
    private static String fallbackImageExtension = ".jpg";

    private String filesFolder;
    private String baseUrl;
    private String author;

    private Map<String, Mix> repo = new HashMap<>();

    MixService(String filesFolder, String hostName, String applicationPath, String pmsAuthor) {
        this.filesFolder = filesFolder;
        this.baseUrl = hostName + applicationPath;
        this.author = pmsAuthor;
    }


    void refreshRepo() {
        repo.clear();
        try {
            Stream<Path> walk = Files.walk(Paths.get(filesFolder));
            walk.filter(path -> path.toString().endsWith(".info.json"))
                    .map(this::mixFromPath)
                    .forEach(mix -> repo.put(mix.getId(), mix));
        } catch (IOException e) {
            log.error("{}: {}", e.getClass().toString(), e.getMessage());
            log.error("Is the correct files folder configured? Check pms.filesFolder setting.");
        }
    }

    private Mix mixFromPath(Path path) {
        try {
            Map<?, ?> infoJson = new ObjectMapper().readValue(path.toFile(), Map.class);
            String id = (String) infoJson.get("id");
            String title = (String) infoJson.get("title");
            String baseFileName = path.toString().replace(".info.json", "").replaceFirst(filesFolder, "");

            path.toString().replace(".m4a", "").replaceFirst(filesFolder, "");
            long publishMillis = Files.readAttributes(path, BasicFileAttributes.class).creationTime().toMillis();
            Date publishDate = new Date(publishMillis);

            String imageFile = urlEncode(baseFileName) + determineImageFormat(baseFileName);
            String audioFile = urlEncode(baseFileName) + ".m4a";
            return new Mix(id, title, author,
                baseUrl + "/file/" + urlEncode(baseFileName) + determineImageFormat(baseFileName),
                baseUrl + "/file/" + urlEncode(baseFileName) + ".m4a",
                           imageFile, audioFile, publishDate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Flux<Mix> mixStream() {
        refreshRepo();
        return Flux.fromStream(repo.values().stream());
    }

    public Mono<Mix> mixMono(String id) {
        refreshRepo();
        return Mono.just(repo.get(id));
    }

    private String determineImageFormat(String name) {
        return imageExtensions.stream().filter(s -> filePresent(name, s))
                              .findFirst()
                              .orElseGet(this::fallbackImageExtension);
    }

    private String fallbackImageExtension() {
        return fallbackImageExtension;
    }

    private boolean filePresent(String name, String extension) {
        try {
            return Files.walk(Paths.get(filesFolder)).anyMatch(path -> path.toString().endsWith(name + extension));
        } catch (IOException e) {
            log.error("error checking if file is present {}{}", name, extension);
        }
        return false;
    }

    String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
