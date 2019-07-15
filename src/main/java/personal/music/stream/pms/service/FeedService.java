package personal.music.stream.pms.service;

import java.util.Collections;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.EntryInformationImpl;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import personal.music.stream.pms.mix.Mix;
import reactor.core.publisher.Mono;

@Service
public class FeedService {

    private Logger log = LoggerFactory.getLogger(FeedService.class);

    private String baseUrl;
    private MixService mixService;

    public FeedService(MixService mixService, String hostName, String applicationPath) {
        this.mixService = mixService;
        this.baseUrl = hostName + applicationPath;
    }

    public Mono<SyndFeed> syndFeed() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("Personal Music Stream - Jaydee music");
        feed.setDescription("Stream you personal music");
        feed.setAuthor("Jaydee");
        feed.setLink(baseUrl + "/rss");

        SyndLink link = new SyndLinkImpl();
        link.setRel("self");
        link.setHref(baseUrl + "/rss");
        link.setTitle("Personal Music Stream - Jaydee music");
        feed.setLinks(Collections.singletonList(link));

        SyndImageImpl image = new SyndImageImpl();
        image.setUrl(baseUrl + "/file/channel-image.jpg");
        image.setTitle("Personal Music Stream - Jaydee music");
        image.setLink(baseUrl + "/rss");
        image.setHeight(144);
        image.setWidth(144);
        feed.setImage(image);

        mixService.mixStream()
                .map(this::toEntry)
                .collectList()
                .subscribe(feed::setEntries);

        return Mono.just(feed);
    }

    private SyndEntry toEntry(Mix mix) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setLink(mix.getFullUrl());
        entry.setTitle(mix.getName());
        entry.setUri(mix.getFullUrl());
        entry.setPublishedDate(mix.getPublishedDate());
        SyndCategory category = new SyndCategoryImpl();
        category.setName("music");
        entry.setCategories(Collections.singletonList(category));

        EntryInformation entryInformation = new EntryInformationImpl();
        entryInformation.setTitle(mix.getName());
        entry.getModules().add(entryInformation);

        return entry;
    }

    public Mono<String> syndFeedString() {
        return syndFeed().map(this::outputFeed);
    }

    private String outputFeed(SyndFeed f) {
        String s = null;
        try {
            s = new SyndFeedOutput().outputString(f);
        } catch (FeedException e) {
            e.printStackTrace();
        }
        return s;
    }
}
