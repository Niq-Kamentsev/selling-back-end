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
	private Map<Long, Map<Long, SseEmitter>> emitters = new HashMap<>();

	public SseEmitter getEmitter(Long userId, Long emitterId) {
		Map<Long, SseEmitter> userEmitters = emitters.get(userId);
		SseEmitter emitter = null;
		if (userEmitters != null) {
			emitter = userEmitters.get(emitterId);
		} else {
			userEmitters = new HashMap<>();
			emitters.put(userId, userEmitters);
		}
		if (emitter == null) {
			emitter = new SseEmitter(EMITTER_TIMEOUT);
			emitter.onTimeout(() -> {
				this.removeEmitter(userId, emitterId);
				System.out.println("onTimeout: " + userId);
			});
			userEmitters.put(emitterId, emitter);
		}
		return emitter;
	}

	public synchronized void removeEmitter(Long userId, Long emitterId) {
		Map<Long, SseEmitter> userEmitters = emitters.get(userId);
		if (userEmitters != null) {
			SseEmitter emitter = userEmitters.get(emitterId);
			if (emitter != null) {
				emitter.complete();
			}
			userEmitters.remove(emitterId);
		}
	}

	public void pushNotification(Long targetUserId, Long senderUserId) {
		Map<Long, SseEmitter> userEmitters = emitters.get(targetUserId);
		if (userEmitters != null) {
			for (SseEmitter emitter : userEmitters.values()) {
				if (emitter != null) {
					try {
						emitter.send(SseEmitter.event().data(senderUserId, MediaType.APPLICATION_JSON));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
