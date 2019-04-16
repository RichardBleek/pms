package personal.music.stream.pms.feed;

import java.util.Collections;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.rometools.rome.feed.rss.Category;
import com.rometools.rome.feed.rss.Image;
import com.rometools.rome.feed.rss.Item;

import personal.music.stream.pms.mix.Mix;
import personal.music.stream.pms.mix.MixService;
import reactor.core.publisher.Mono;

@Service
public class FeedService {

    private MixService mixService;

    public FeedService(MixService mixService) {
        this.mixService = mixService;
    }

    public Mono<Channel> rss() {
        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle("Personal Music Stream - Jaydee music");
        channel.setDescription("Stream you personal music");
        channel.setLink("http://rbleek.com/jaydee/rss");
        channel.setUri("http://rbleek.com/jaydee/rss");
        channel.setGenerator("Generic Generator");
        channel.setManagingEditor("Team Anything");
 
        Image image = new Image();
        image.setUrl("https://rbleek.com/jaydee/file/channel-image.jpg");
        image.setTitle("Personal music stream");
        image.setHeight(288);
        image.setWidth(288);
        channel.setImage(image);
 
        Date postDate = new Date();
        channel.setPubDate(postDate);
 
        mixService.mixStream().map(this::toItem).subscribe(channel::addItem);

        return Mono.just(channel);
    }

    public Item toItem(Mix mix) {
        Item item = new Item();
        item.setLink(mix.getFullUrl());
        item.setTitle(mix.getName());
        item.setUri(mix.getFullUrl());
        Category category = new Category();
        category.setValue("Music");
        item.setCategories(Collections.singletonList(category));
        return item;
    }
}
