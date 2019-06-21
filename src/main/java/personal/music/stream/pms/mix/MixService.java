package personal.music.stream.pms.mix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MixService {

    private Logger log = LoggerFactory.getLogger(MixService.class);

    private String filesFolder;
    private String baseUrl;

    MixService(String filesFolder, String hostName, String applicationPath) {
        this.filesFolder = filesFolder;
        this.baseUrl = hostName + applicationPath;
    }

    private Map<String, Mix> repo = new HashMap<>();

    void fillRepo() {
        try {
            Stream<Path> walk = Files.walk(Paths.get(filesFolder));
            walk.filter(path -> path.toString().endsWith(".m4a"))
                    .map(this::mixFromPath)
                    .forEach(mix -> repo.put(mix.getId(), mix));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Mix mixFromPath(Path path) {
        String name = path.toString().replace(".m4a", "");
        Date publishDate = new Date();
        return new Mix(name, name,
                baseUrl + "/file/" + name + ".jpg",
                baseUrl + "/file/" + name + ".m4a",
                publishDate);
    }

    public Flux<Mix> mixStream() {
        fillRepo();
        return Flux.fromStream(repo.values().stream());
    }

    public Mono<Mix> mixMono(String id) {
        fillRepo();
        return Mono.just(repo.get(id));
    }
}
