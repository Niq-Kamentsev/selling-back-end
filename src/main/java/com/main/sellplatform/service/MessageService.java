package com.main.sellplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.sellplatform.persistence.dao.MessageDao;
import com.main.sellplatform.persistence.entity.Message;
import com.main.sellplatform.persistence.entity.MessageChannel;

@Service
public class MessageService {
	private final MessageDao messageDao;
	
	@Autowired
	public MessageService(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
	public List<MessageChannel> getChannels(Long userId){
		return messageDao.getChannels(userId);
	}
	
	public List<Message> getMessages(Long currentUserId, Long targetUser){
		return null;
	}
}
