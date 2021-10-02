package personal.music.stream.pms.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.reactive.function.server.*;

import personal.music.stream.pms.mix.*;
import personal.music.stream.pms.service.*;

@Configuration
public class PmsRouter {

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

