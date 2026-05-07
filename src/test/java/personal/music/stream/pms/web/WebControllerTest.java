package personal.music.stream.pms.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import personal.music.stream.pms.mix.Mix;
import personal.music.stream.pms.service.MixService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WebControllerTest {

    private MixService mixService;
    private WebController webController;

    @BeforeEach
    void setUp() {
        mixService = Mockito.mock(MixService.class);
        webController = new WebController(mixService);
    }

    @Test
    void player_shouldReturnListViewWithMixes() {
        Model model = Mockito.mock(Model.class);
        Mix mix1 = new Mix("mix-1", "Test Mix 1", "Test Author 1", "http://image1.jpg", "http://audio1.m4a", "image1.jpg", "audio1.m4a", new Date(2024, 0, 1));
        Mix mix2 = new Mix("mix-2", "Test Mix 2", "Test Author 2", "http://image2.jpg", "http://audio2.m4a", "image2.jpg", "audio2.m4a", new Date(2024, 0, 2));
        
        List<Mix> testMixes = new ArrayList<>(List.of(mix1, mix2));
        
        Flux<Mix> flux = Flux.fromIterable(testMixes);
        
        when(mixService.mixStream()).thenReturn(flux);
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        
        String result = webController.player(model);
        
        assertEquals("list", result);
        verify(model, atLeastOnce()).addAttribute(eq("mixes"), any(List.class));
    }

    @Test
    void player_withMixId_shouldReturnPlayerView() {
        Model model = Mockito.mock(Model.class);
        String mixId = "test-mix-id";
        Mix testMix = new Mix(mixId, "Test Mix", "Test Author", "http://image.jpg", "http://audio.m4a", "image.jpg", "audio.m4a", new Date(2024, 0, 1));
        
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        when(mixService.mixMono(mixId)).thenReturn(Mono.just(testMix));
        
        String result = webController.player(mixId, model);
        
        assertEquals("player", result);
        verify(model).addAttribute(eq("mix"), any());
    }

    @Test
    void player_withEmptyMixes_shouldReturnListView() {
        Model model = Mockito.mock(Model.class);
        
        Flux<Mix> emptyFlux = Flux.empty();
        
        when(mixService.mixStream()).thenReturn(emptyFlux);
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        
        String result = webController.player(model);
        
        assertEquals("list", result);
    }

    @Test
    void player_withNullMixId_shouldReturnPlayerView() {
        Model model = Mockito.mock(Model.class);
        
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        when(mixService.mixMono(null)).thenReturn(Mono.empty());
        
        String result = webController.player(null, model);
        
        assertEquals("player", result);
    }

    @Test
    void player_withSpecialCharactersInMixId_shouldReturnPlayerView() {
        Model model = Mockito.mock(Model.class);
        String mixId = "mix-with-special_chars.123";
        Mix testMix = new Mix(mixId, "Test Mix", "Test Author", "http://image.jpg", "http://audio.m4a", "image.jpg", "audio.m4a", new Date(2024, 0, 1));
        
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        when(mixService.mixMono(mixId)).thenReturn(Mono.just(testMix));
        
        String result = webController.player(mixId, model);
        
        assertEquals("player", result);
    }

    @Test
    void constructor_shouldInitializeWithMixService() {
        assertNotNull(webController);
        assertInstanceOf(WebController.class, webController);
    }
    
    @Test
    void player_withSingleMix_shouldReturnListView() {
        Model model = Mockito.mock(Model.class);
        Mix mix = new Mix("mix-1", "Single Mix", "Author", "http://image.jpg", "http://audio.m4a", "image.jpg", "audio.m4a", new Date(2024, 0, 1));
        
        Flux<Mix> flux = Flux.just(mix);
        
        when(mixService.mixStream()).thenReturn(flux);
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        
        String result = webController.player(model);
        
        assertEquals("list", result);
        verify(model).addAttribute(eq("mixes"), any(List.class));
    }
    
    @Test
    void player_withMixId_returnsCorrectModelAttribute() {
        Model model = Mockito.mock(Model.class);
        String mixId = "unique-mix-id";
        
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        when(mixService.mixMono(mixId)).thenReturn(Mono.empty());
        
        webController.player(mixId, model);
        
        verify(model).addAttribute(eq("mix"), any(Mono.class));
    }
}
