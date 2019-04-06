package personal.music.stream.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import personal.music.stream.pms.mix.FileService;
import personal.music.stream.pms.mix.Mix;
import personal.music.stream.pms.mix.MixService;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class PmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmsApplication.class, args);
	}

    @Bean
    RouterFunction<?> routes(MixService mixService, FileService fileService){
        return route(RequestPredicates.GET("/mixes"),
                request -> ServerResponse.ok().body(mixService.mixStream(), Mix.class))
                .andRoute(RequestPredicates.GET("/mixes/{id}"),
                        request -> ServerResponse.ok().body(
                                mixService.mixMono(request.pathVariable("id")), Mix.class))
                .andRoute(RequestPredicates.GET("/file/{fileName}"),
                        request -> ServerResponse.ok().body(
                                fileService.streamFile(request.pathVariable("fileName")), Resource.class
                        ));
    }

}
