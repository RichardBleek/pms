package personal.music.stream.pms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import personal.music.stream.pms.mix.Mix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * Tests for {@link MixService} focusing on repository building and format fallback logic.
 */
class MixServiceTest {

    private static final String HOST = "http://localhost";
    private static final String APP_PATH = "/pms";
    private static final String AUTHOR = "testAuthor";

    @TempDir
    Path tempDir;

    private MixService service;

    @BeforeEach
    void setUp() {
        // MixService constructor is package‑private; we are in the same package.
        service = new MixService(tempDir.toString(), HOST, APP_PATH, AUTHOR);
    }

    @Test
    void mixMono_returnsMixWithFallbackExtensions_whenNoMediaFilesExist() throws IOException {
        // create a minimal .info.json file
        String json = "{\"id\":\"mix1\",\"title\":\"Test Mix\",\"channel\":\"TestChannel\",\"upload_date\":\"20240101\"}";
        Path infoFile = Files.writeString(tempDir.resolve("mix1.info.json"), json);

        // No image or audio files are created – service must use fallbacks (.jpg and .webm)
        Mono<Mix> mono = service.mixMono("mix1");

        StepVerifier.create(mono)
                .assertNext(mix -> {
                    // URL should contain encoded base name (empty string after removal of folder prefix)
                    String base = "/file/" + "mix1" + ".jpg"; // image fallback
                    String expectedImageUrl = HOST + APP_PATH + base;
                    String expectedAudioUrl = HOST + APP_PATH + "/file/" + "mix1" + ".webm";
                    // Verify fields
                    assert mix.getId().equals("mix1");
                    assert mix.getName().equals("Test Mix");
                    assert mix.getAuthor().equals("TestChannel");
                    assert mix.getImageUrl().equals(expectedImageUrl);
                    assert mix.getFullUrl().equals(expectedAudioUrl);
                    // Files stored on server (relative paths) use the fallback extensions as well
                    assert mix.getImageFile().equals("mix1.jpg");
                    assert mix.getAudioFile().equals("mix1.webm");
                })
                .verifyComplete();
    }

    @Test
    void mixStream_includesMultipleMixes_andDetectsExistingMediaFiles() throws IOException {
        // Mix 1 – has a jpg image and mp3 audio
        String json1 = "{\"id\":\"m1\",\"title\":\"First\",\"channel\":\"Chan1\",\"upload_date\":\"20240102\"}";
        Files.writeString(tempDir.resolve("m1.info.json"), json1);
        Files.createFile(tempDir.resolve("m1.jpg"));
        Files.createFile(tempDir.resolve("m1.mp3"));

        // Mix 2 – has no media files, should fall back
        String json2 = "{\"id\":\"m2\",\"title\":\"Second\",\"channel\":\"Chan2\",\"upload_date\":\"20240103\"}";
        Files.writeString(tempDir.resolve("m2.info.json"), json2);

        Flux<Mix> flux = service.mixStream();

        StepVerifier.create(flux.collectList())
                .assertNext(list -> {
                    assert list.size() == 2;
                    java.util.Map<String, Mix> map = list.stream().collect(java.util.stream.Collectors.toMap(Mix::getId, m -> m));
                    Mix m1 = map.get("m1");
                    assert m1 != null;
                    assert m1.getImageFile().endsWith(".jpg");
                    assert m1.getAudioFile().endsWith(".mp3");
                    Mix m2 = map.get("m2");
                    assert m2 != null;
                    assert m2.getImageFile().endsWith(".jpg");
                    assert m2.getAudioFile().endsWith(".webm");
                })
                .verifyComplete();
    }

    @Test
    void urlEncode_encodesSpacesCorrectly() {
        String encoded = service.urlEncode("a b/c");
        // '+' should be replaced with %20
        assert encoded.equals("a%20b%2Fc");
    }
}
