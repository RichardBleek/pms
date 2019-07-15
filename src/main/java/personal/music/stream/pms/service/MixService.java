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
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MixService {

    private Logger log = LoggerFactory.getLogger(MixService.class);

    private String filesFolder;
    private String baseUrl;
    private Map<String, Mix> repo = new HashMap<>();

    MixService(String filesFolder, String hostName, String applicationPath) {
        this.filesFolder = filesFolder;
        this.baseUrl = hostName + applicationPath;
    }


    void refreshRepo() {
        repo.clear();
        try {
            Stream<Path> walk = Files.walk(Paths.get(filesFolder));
            walk.filter(path -> path.toString().endsWith(".m4a"))
                    .map(this::mixFromPath)
                    .forEach(mix -> repo.put(mix.getId(), mix));
        } catch (IOException e) {
            log.error("{}: {}", e.getClass().toString(), e.getMessage());
            log.error("Is the correct files folder configured? Check pms.filesFolder setting.");
        }
    }

    private Mix mixFromPath(Path path) {
        try {
            String name = path.toString().replace(".m4a", "").replaceFirst(filesFolder, "");
            long publishMillis = Files.readAttributes(path, BasicFileAttributes.class).creationTime().toMillis();
            Date publishDate = new Date(publishMillis);
            String owner = Files.getFileAttributeView(path, FileOwnerAttributeView.class).getOwner().getName();
            return new Mix(name, name, owner,
                baseUrl + "/file/" + urlEncode(name) + determineImageFormat(name),
                baseUrl + "/file/" + urlEncode(name) + ".m4a", publishDate);
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
        try {
            if(Files.walk(Paths.get(filesFolder)).anyMatch(path -> path.toString().endsWith(name + ".jpg"))) {
                return ".jpg";
            }
            if(Files.walk(Paths.get(filesFolder)).anyMatch(path -> path.toString().endsWith(name + ".png"))) {
                return ".png";
            }
            return ".jpg";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
