package personal.music.stream.pms.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
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
        Assert.assertEquals("Slipstream%20Currents", mixService.urlEncode("Slipstream Currents"));
    }
}