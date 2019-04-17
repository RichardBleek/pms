package personal.music.stream.pms.mix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MixService {

    private Logger log = LoggerFactory.getLogger(MixService.class);

    @Value("${pms.files-folder}")
    String filesFolder;

    private ResourceLoader resourceLoader;

    private Map<String, Mix> repo = new HashMap<>();

    MixService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void fillRepo() {
        try {
            Stream<Path> walk = Files.walk(Paths.get(filesFolder));
            walk.map(Path::toString)
                    .filter(f -> f.endsWith(".m4a"))
                    .map(s -> s.replaceFirst(filesFolder, ""))
                    .map(s -> s.replaceFirst(".m4a", ""))
                    .forEach(s -> repo.put(
                            s, new Mix(s, s, "http://rbleek.com/jaydee/file/"+s+".jpg",
                                    "http://rbleek.com/jaydee/file/"+s+".m4a")));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
