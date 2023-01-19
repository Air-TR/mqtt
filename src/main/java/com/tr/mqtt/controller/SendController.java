package com.tr.mqtt.controller;

import com.tr.mqtt.config.MqttProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {
    @Autowired
    private MqttProviderConfig providerClient;

    @GetMapping("/send")
    public String sendMessage(){
        try {
            providerClient.publish(0, false, "test_topic", "hello 123");
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }

//    @GetMapping("/send")
//    public String sendMessage(int qos,boolean retained,String topic,String message){
//        try {
//            providerClient.publish(qos, retained, topic, message);
//            return "发送成功";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "发送失败";
//        }
//    }

}
