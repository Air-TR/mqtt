package com.tr.mqtt.controller;

import com.tr.mqtt.callback.MqttConsumerCallBack;
import com.tr.mqtt.config.MqttConsumerConfig;
import com.tr.mqtt.config.MqttProviderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MqttController {

    @Value("${spring.mqtt.consumerClientId}")
    private String consumerClientId;

    @Resource
    private MqttProviderConfig providerClient;

    @Resource
    private MqttConsumerConfig consumerClient;

    @GetMapping("/index")
    public int index() {
        return MqttConsumerCallBack.index;
    }

    @GetMapping("/sendMsg")
    public void sendMessage() {
        providerClient.publish(0, false, "test_topic", "hello 123");
    }

    @GetMapping("/consumer/connect")
    public String connect(){
        consumerClient.connect();
        return consumerClientId + " 连接到服务器";
    }

    @GetMapping("/consumer/disConnect")
    public String disConnect(){
        consumerClient.disConnect();
        return consumerClientId + " 与服务器断开连接";
    }

}
