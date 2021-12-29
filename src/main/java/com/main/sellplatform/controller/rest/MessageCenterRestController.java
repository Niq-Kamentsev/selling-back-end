package com.main.sellplatform.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.main.sellplatform.controller.dto.messagedto.MessageDTO;
import com.main.sellplatform.persistence.entity.User;
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
	@GetMapping(value = "/getMessageChannenls", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getMessageChannels() {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(messageService.getChannels(userByEmail.getId()));
	}

	@PreAuthorize("hasAnyAuthority('user:read')")
	@GetMapping(value = "/getMessages", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getMessages(@RequestParam Long targetUser, @RequestParam Long bidId) {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(messageService.getMessages(userByEmail.getId(), targetUser, bidId, null));
	}
	
	@PreAuthorize("hasAnyAuthority('user:read')")
	@GetMapping(value = "/getNewMessages", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getNewMessages(@RequestParam Long targetUser, @RequestParam Long bidId, @RequestParam Long lastMessageId) {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(messageService.getMessages(userByEmail.getId(), targetUser, bidId, lastMessageId));
	}

	@PreAuthorize("hasAnyAuthority('user:read')")
	@GetMapping(value = "/subscribe")
	public SseEmitter subscribe() {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		SseEmitter emitter = emitterService.getEmitter(userByEmail.getId());
		System.out.println("user with id:" + userByEmail.getId() + " subscribed");
		return emitter;
	}

	@PreAuthorize("hasAnyAuthority('user:read')")
	@GetMapping(value = "/unsubscribe")
	public ResponseEntity<?> unsubscribe() {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		emitterService.removeEmitter(userByEmail.getId());
		return ResponseEntity.ok().build();
	}

	@Transactional
	@PreAuthorize("hasAnyAuthority('user:write')")
	@PostMapping(value = "/sendMessage", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> sendMessage(@RequestBody MessageDTO body) {
		User userByEmail = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		boolean isOk = messageService.saveMessage(body, userByEmail.getId());
		if (isOk) {
			emitterService.pushNotification(body.getReceiver(), userByEmail.getId());
			emitterService.pushNotification(userByEmail.getId(), body.getReceiver());
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
