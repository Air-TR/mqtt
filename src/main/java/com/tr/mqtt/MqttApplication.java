package com.tr.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动项目可能遇到的问题：要同时启动 Provider 和 Consumer，就要给 Provider 和 Consumer 设置不同的 client-id
 */
@SpringBootApplication
public class MqttApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttApplication.class, args);
	}

}
