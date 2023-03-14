package com.tr.mqtt.service.impl;

import com.tr.mqtt.entity.Message;
import com.tr.mqtt.jpa.MessageRepository;
import com.tr.mqtt.service.MessageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author taorun
 * @date 2023/2/1 16:48
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageRepository messageRepository;

    @Async
    @Override
    public void save(Message message) {
        messageRepository.save(message);
        System.out.println(message.getId());
    }

}
