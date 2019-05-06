package personal.music.stream.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import personal.music.stream.pms.feed.FeedService;
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
    String filesFolder(Environment environment) {
	    return environment.getProperty("pms.filesFolder");
    }

	@Bean
    String hostName(Environment environment) {
	   return environment.getProperty("pms.hostName");
    }

	@Bean
    String applicationPath(Environment environment) {
	    return environment.getProperty("pms.applicationPath");
    }

    @Bean
    RouterFunction<?> routes(MixService mixService, FileService fileService, FeedService feedService, String applicationPath){
        return route(RequestPredicates.GET(applicationPath+"/mixes"),
                request -> ServerResponse.ok().body(mixService.mixStream(), Mix.class))
                .andRoute(RequestPredicates.GET(applicationPath+"/mixes/{id}"),
                        request -> ServerResponse.ok().body(
                                mixService.mixMono(request.pathVariable("id")), Mix.class))
                .andRoute(RequestPredicates.GET(applicationPath+"/file/{fileName}"),
                        request -> ServerResponse.ok().body(
                                fileService.streamFile(request.pathVariable("fileName")), Resource.class
                        ))
                .andRoute(RequestPredicates.GET(applicationPath+"/rss"),
                        request -> ServerResponse.ok().contentType(MediaType.APPLICATION_RSS_XML).body(
                               feedService.syndFeedString(), String.class
                        ));
    }

}
