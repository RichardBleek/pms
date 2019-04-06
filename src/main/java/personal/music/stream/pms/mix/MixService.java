package personal.music.stream.pms.mix;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MixService {

    private Map<String, Mix> repo = new HashMap<>();

    MixService() {
        Stream.of("semblance","slipstream-currents").forEach(s -> repo.put(
                  s, new Mix(s, s, "http://localhost:8080/file/"+s+".jpg",
                       "http://localhost:8080/file/"+s+".m4a")));
    }

    public Flux<Mix> mixStream() {
        return Flux.fromStream(repo.values().stream());
    }

    public Mono<Mix> mixMono(String id) {
        return Mono.just(repo.get(id));
    }
}
