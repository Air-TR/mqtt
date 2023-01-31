package com.tr.mqtt.config;

import javax.annotation.PostConstruct;

import com.tr.mqtt.callback.MqttConsumerCallBack;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConsumerConfig {
    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.consumerClientId}")
    private String consumerClientId;

    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    /**
     * 客户端对象
     */
    private MqttClient client;

    /**
     * 在 bean 初始化后连接到服务器
     */
    @PostConstruct
    public void init() {
        connect();
    }

    /**
     * 客户端连接服务端
     */
    public void connect() {
        try {
            // 创建 MQTT 客户端对象
            client = new MqttClient(hostUrl, consumerClientId, new MemoryPersistence());
            // 连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 是否清空 session，设置为 false 表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            // 设置为 true 表示每次连接到服务端都是以新的身份
            options.setCleanSession(true);
            // 设置连接用户名
            options.setUserName(username);
            // 设置连接密码
            options.setPassword(password.toCharArray());
            // 设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            // 设置心跳时间 单位为秒，表示服务器每隔 1.5 * 20 秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            // 设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (consumerClientId + "与服务器断开连接").getBytes(), 0, false);
            // 设置回调
            client.setCallback(new MqttConsumerCallBack());
            client.connect(options);
            // 订阅主题
            // 消息等级（与 topics[] 一一对应），服务端将按照指定等级给订阅了主题的客户端推送消息
            int[] qos = {0};
//            int[] qos = {0, 1, 2};
            // 主题（与 qos[] 一一对应）
            String[] topics = {"QPS"};
//            String[] topics = {defaultTopic, "topic2", "topic3"};
            // 订阅主题
            client.subscribe(topics, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     */
    public void disConnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅主题
     */
    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
