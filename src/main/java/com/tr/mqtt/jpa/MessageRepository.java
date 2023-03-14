package com.tr.mqtt.jpa;

import com.tr.mqtt.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
