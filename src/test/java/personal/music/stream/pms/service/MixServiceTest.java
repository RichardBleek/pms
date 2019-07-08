package personal.music.stream.pms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import personal.music.stream.pms.service.MixService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MixServiceTest {

    @Autowired
    MixService mixService;

    @Test
    public void fillRepo() {
        mixService.fillRepo();
    }
}