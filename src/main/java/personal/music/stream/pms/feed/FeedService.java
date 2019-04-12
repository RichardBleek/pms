package personal.music.stream.pms.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        channel.setTitle("PMS Personal Music Stream");
        channel.setDescription("Stream you personal music");
        channel.setLink("https://pm.stream");
        channel.setUri("https://pm.stream");
        channel.setGenerator("Dieselgenerator");
        channel.setManagingEditor("Team Anything");
 
        Image image = new Image();
        image.setUrl("https://howtodoinjava.com/wp-content/uploads/2015/05/howtodoinjava_logo-55696c1cv1_site_icon-32x32.png");
        image.setTitle("Personal music stream");
        image.setHeight(32);
        image.setWidth(32);
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