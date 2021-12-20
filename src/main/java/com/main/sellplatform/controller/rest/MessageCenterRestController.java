package com.main.sellplatform.controller.rest;

import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.main.sellplatform.controller.dto.messagedto.MessageDTO;

import com.main.sellplatform.service.EmitterService;
import com.main.sellplatform.service.MessageService;
import com.main.sellplatform.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/messageCenter")
public class MessageCenterRestController {

	private final MessageService messageService;
	private final UserService userService;
	private final EmitterService emitterService;

	@Autowired
	public MessageCenterRestController(final MessageService messageService, final UserService userService,
			final EmitterService emitterService) {
		this.messageService = messageService;
		this.userService = userService;
		this.emitterService = emitterService;
	}

	@PreAuthorize("hasAnyAuthority('user:read')")
	@GetMapping(value = "/getMessageChannenls")
	public ResponseEntity<?> getMessageChannels() {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(messageService.getChannels(userByEmail.getId()));
	}

	@PreAuthorize("hasAnyAuthority('user:read')")
	@GetMapping(value = "/getMessages")
	public ResponseEntity<?> getMessages(@RequestParam Long targetUser) {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(messageService.getMessages(userByEmail.getId(), targetUser));
	}
	
	@PreAuthorize("hasAnyAuthority('user:read')")
	@GetMapping(value = "/subscription")
	public SseEmitter subscribe() {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		emitterService.addEmitter(emitter, userByEmail.getId());
		System.out.println("user with id:" + userByEmail.getId() + " subscribed");
		return emitter;
	}

	@PreAuthorize("hasAnyAuthority('user:write')")
	@PostMapping(value = "/sendMessage")
	public ResponseEntity<?> sendMessage(@RequestParam Long targetUser, @RequestBody MessageDTO body) {
		emitterService.pushNotification(targetUser, "test", "Test");
		return ResponseEntity.ok().build();
	}
}
