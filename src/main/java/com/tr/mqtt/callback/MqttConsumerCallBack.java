package com.tr.mqtt.callback;

import com.tr.mqtt.entity.Message;
import com.tr.mqtt.jpa.MessageRepository;
import com.tr.mqtt.service.MessageService;
import com.tr.mqtt.service.RedisService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class MqttConsumerCallBack implements MqttCallbackExtended {

    public volatile static int index = 0;

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisService redisService;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    public MqttConsumerCallBack(StringRedisTemplate stringRedisTemplate, RedisService redisService, MessageRepository messageRepository, MessageService messageService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisService = redisService;
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("与服务器断开连接，可重连");
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        index++;
        System.out.println(++index);
        stringRedisTemplate.opsForValue().set("QPS:" + index, message.toString(), 300, TimeUnit.SECONDS);
//        redisService.set(++index, message.toString());
//        messageRepository.save(new Message(index, message.toString()));
//        messageService.save(new Message(++index, message.toString()));

//        System.out.println(String.format("接收消息主题: %s", topic));
//        System.out.println(String.format("接收消息Qos: %d", message.getQos()));
//        System.out.println(String.format("接收消息内容: %s", new String(message.getPayload())));
//        System.out.println(String.format("接收消息retained: %b", message.isRetained()));
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // 因为这里是消费者，不需要实现此方法
    }

    @Override
    public void connectComplete(boolean reconnect, String mqttUrl) {
        // 在 MqttConsumerConfig -> MqttClient.setCallback() 中实现，用于自动重连后订阅主题，否则重连后不订阅任何主题
    }

}
