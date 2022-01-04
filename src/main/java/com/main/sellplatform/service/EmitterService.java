package com.main.sellplatform.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class EmitterService {

	private static final long EMITTER_TIMEOUT = TimeUnit.MINUTES.toMillis(1);
	private Map<Long, SseEmitter> emitters = new HashMap<>();

	public SseEmitter getEmitter(Long userId) {
		SseEmitter emitter = emitters.get(userId);
		if(emitter == null) {
			emitter = new SseEmitter(EMITTER_TIMEOUT);
			emitter.onTimeout(() -> {
				this.removeEmitter(userId);
				System.out.println("onTimeout: " + userId);
			});
			emitters.put(userId, emitter);
		}
		return emitter;
	}

	public void removeEmitter(Long userId) {
		SseEmitter emitter = emitters.get(userId);
		if(emitter != null) {
			emitter.complete();
		}
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
