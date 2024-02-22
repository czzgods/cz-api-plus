package com.cz.czapi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisLimiterManagerTest {
    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Test
    void testXianliu() throws InterruptedException {
        String user = "1";
        for (int i = 0; i < 2; i++) {
            redisLimiterManager.doRateLimit(user);
            System.out.println("success");
        }
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimit(user);
            System.out.println("成功");
        }
    }
}