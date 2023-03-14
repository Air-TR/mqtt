package com.tr.mqtt.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.tr.mqtt.callback.MqttConsumerCallBack;
import com.tr.mqtt.jpa.MessageRepository;
import com.tr.mqtt.service.MessageService;
import com.tr.mqtt.service.RedisService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class MqttConsumerConfig {
    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String url;

    @Value("${spring.mqtt.consumerClientId}")
    private String consumerClientId;

    @Value("${spring.mqtt.topic}")
    private String[] topic;

    @Value("${spring.mqtt.qos}")
    private int[] qos;

    @Value("${spring.mqtt.automaticReconnect}")
    private Boolean automaticReconnect;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisService redisService;

    @Resource
    private MessageRepository messageRepository;

    @Resource
    private MessageService messageService;

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
            client = new MqttClient(url, consumerClientId, new MemoryPersistence());
            // 连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            options.setServerURIs(new String[]{url});
            // 是否清空 session，设置为 false 表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            // 设置为 true 表示每次连接到服务端都是以新的身份
            options.setCleanSession(true);
            // 设置连接用户名
            options.setUserName(username);
            // 设置连接密码
            options.setPassword(password.toCharArray());
            // 设置超时时间，单位为秒
            options.setConnectionTimeout(10);
            // 设置心跳时间 单位为秒，表示服务器每 30 秒向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(30);
            // 设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (consumerClientId + "与服务器断开连接").getBytes(), 0, false);
            // 断线自动重连
            options.setAutomaticReconnect(automaticReconnect);
            // 设置回调
            client.setCallback(new MqttConsumerCallBack(stringRedisTemplate, redisService, messageRepository, messageService) {
                @Override
                public void connectComplete(boolean reconnect, String mqttUrl) {
                    try {
                        client.subscribe(topic, qos);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            client.connect(options);
            // 订阅主题
            client.subscribe(topic, qos);
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
