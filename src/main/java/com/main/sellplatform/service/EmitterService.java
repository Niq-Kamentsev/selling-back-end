package com.main.sellplatform.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class EmitterService {

	private Map<Long, SseEmitter> emitters = new HashMap<>();

	public void addEmitter(SseEmitter emitter, Long userId) {
		emitter.onTimeout(() -> {
			emitters.remove(userId);
			System.out.println("onTimeout: " + userId);
		});
		emitters.put(userId, emitter);
	}
	
	public void removeEmitter(Long userId) {
		emitters.remove(userId);
	}

	public void pushNotification(Long targetUserId, Long senderUserId) {
		SseEmitter emitter = emitters.get(targetUserId);
		if (emitter != null) {
			try {
				emitter.send(senderUserId, MediaType.APPLICATION_JSON);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
