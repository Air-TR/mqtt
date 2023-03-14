package com.tr.mqtt.service.impl;

import com.tr.mqtt.service.RedisService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author taorun
 * @date 2023/2/1 16:00
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Async
    @Override
    public void set(int index, String message) {
        stringRedisTemplate.opsForValue().set("QPS:" + index, message.toString(), 300, TimeUnit.SECONDS);
        System.out.println(index);
    }

}
