package com.main.sellplatform.controller.rest;

import com.main.sellplatform.entitymanager.testobj.Message;
import com.main.sellplatform.persistence.dao.MessageDao;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api")
public class UserRestController {
	private final UserService userService;
	private final MessageDao messageDao;

	@Autowired
	public UserRestController(final UserService userService, final MessageDao messageDao) {
		this.userService = userService;
		this.messageDao = messageDao;
	}

	@PreAuthorize("hasAnyAuthority('admin:read')")
	@GetMapping(value = "/getUsers", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public List<User> getUsersJdbc() {
		return userService.getUsers();
	}

	@PreAuthorize("hasAnyAuthority('user:write')")
	@PostMapping(value = "/postUser", produces = { MediaType.APPLICATION_JSON_VALUE })
	public void createUser(@RequestBody User user) {
		System.out.println(user);
		userService.saveUser(user);
	}

	@PreAuthorize("hasAnyAuthority('admin:read')")
	@GetMapping(value = "/getUser{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public User getUser(@PathVariable Long id) {
		return userService.getUser(id);
	}

	@PreAuthorize("hasAnyAuthority('admin:delete')")
	@DeleteMapping(value = "/delete{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}

	@GetMapping(value = "/getWB", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllWB() {
		Message[] msgs = messageDao.getAllMessages();
		com.main.sellplatform.persistence.entity.Message[] result = new com.main.sellplatform.persistence.entity.Message[msgs.length];
		for (int i = 0; i < msgs.length; i++) {
			com.main.sellplatform.persistence.entity.Message tmp = new com.main.sellplatform.persistence.entity.Message();
			Message msg = msgs[i];
			tmp.setId(msg.getId());
			tmp.setReceiver(msg.getReceiver().getId());
			tmp.setSender(msg.getSender().getId());
			tmp.setMessage(msg.getMsg());
			tmp.setDateTime(LocalDateTime.parse(msg.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
			result[i] = tmp;
		}
		return ResponseEntity.ok(result);
	}
}
