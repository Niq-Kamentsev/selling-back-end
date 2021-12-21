package com.main.sellplatform.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.sellplatform.controller.dto.messagedto.MessageDTO;
import com.main.sellplatform.entitymanager.testdao.UserDao2;
import com.main.sellplatform.persistence.dao.MessageDao;
import com.main.sellplatform.persistence.entity.Message;
import com.main.sellplatform.persistence.entity.MessageChannel;

@Service
public class MessageService {
	private final MessageDao messageDao;
	private final UserDao2 userDao;

	@Autowired
	public MessageService(MessageDao messageDao, UserDao2 userDao) {
		this.messageDao = messageDao;
		this.userDao = userDao;
	}

	public List<MessageChannel> getChannels(Long userId) {
		return messageDao.getChannels(userId);
	}

	public List<Message> getMessages(Long currentUserId, Long targetUser) {
		List<Message> result = new ArrayList<>();
		for (com.main.sellplatform.entitymanager.testobj.Message msg : messageDao.getMessages(currentUserId,
				targetUser)) {
			Message message = new Message();
			message.setId(msg.getId());
			message.setSender(msg.getSender().getId());
			message.setReceiver(msg.getReceiver().getId());
			message.setDateTime(msg.getDate());
			message.setMessage(msg.getMsg());
			result.add(message);
		}
		return result;
	}

	public boolean saveMessage(MessageDTO message, Long senderUserId) {
		com.main.sellplatform.entitymanager.testobj.Message result = new com.main.sellplatform.entitymanager.testobj.Message();
		result.setSender(userDao.getUser(senderUserId));
		result.setReceiver(userDao.getUser(message.getReceiver()));
		result.setMsg(message.getMessage());
		result.setDate(Calendar.getInstance().getTime());
		result.setName("");
		result.setDescr("");
		return messageDao.saveMessage(result);
	}
}
