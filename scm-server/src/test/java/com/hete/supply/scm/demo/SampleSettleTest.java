package com.hete.supply.scm.demo;

import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleSettleBaseService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ChenWenLong
 * @date 2022/11/19 16:47
 */
@ActiveProfiles("local")
@SpringBootTest
@RequiredArgsConstructor
@RunWith(SpringRunner.class)
public class SampleSettleTest {

    @Autowired
    private final DevelopSampleSettleBaseService developSampleSettleBaseService;

    @Test
    public void test() {
        developSampleSettleBaseService.countDevelopSampleSettleOrder(null, null);
    }
}
