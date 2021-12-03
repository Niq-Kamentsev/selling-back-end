package com.main.sellplatform.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.service.MessageService;
import com.main.sellplatform.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/messageCenter")
public class MessageCenterRestController {

	private final MessageService messageService;
	private final UserService userService;

	@Autowired
	public MessageCenterRestController(MessageService messageService, UserService userService) {
		this.messageService = messageService;
		this.userService = userService;
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
}
