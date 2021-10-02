package personal.music.stream.pms.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MixServiceTest {

    @Autowired
    MixService mixService;

    @Test
    public void refreshRepo() {
        mixService.refreshRepo();
    }

    @Test
    public void urlEncode() {
        assertThat("Slipstream%20Currents").isEqualTo(mixService.urlEncode("Slipstream Currents"));
    }
}