package com.main.sellplatform.persistence.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.analyzer.Queries;
import com.main.sellplatform.entitymanager.testobj.Message;
import com.main.sellplatform.entitymanager.testobj.User;
import com.main.sellplatform.persistence.entity.MessageChannel;

@Component
public class MessageDao {
	private final EntityManager entityManager;
	private final Queries queries;

	@Autowired
	public MessageDao(EntityManager entityManager, Queries queries) {
		this.entityManager = entityManager;
		this.queries = queries;
	}

	public Message[] getAllMessages() {
		Object[] objects = entityManager.getAllObjects(Message.class);
		Message[] messages = new Message[objects.length];
		for (int i = 0; i < objects.length; ++i) {
			messages[i] = (Message) objects[i];
		}
		return messages;
	}

	public List<MessageChannel> getChannels(Long userId) {
		List<MessageChannel> result = new ArrayList<>();
		Object[] objects = null;
		List<Object> statements = new ArrayList<>();
		statements.add(userId);
		statements.add(userId);
		try {
			objects = entityManager.getObjectsByIdSeq(User.class, queries.selectMessageChannel(), statements);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Object targetObject : objects) {
			User user = (User) targetObject;
			result.add(new MessageChannel(user.getId()));
		}
		return result;
	}

	public List<Message> getMessages(Long currentUserId, Long targetUser) {
		List<Object> statements = new ArrayList<>();
		statements.add(currentUserId);
		statements.add(targetUser);
		statements.add(targetUser);
		statements.add(currentUserId);
		Object[] messages = entityManager.getAllObjects(Message.class, queries.whereMessageMessages(), statements);
		List<Message> result = new ArrayList<>();
		for (Object message : messages) {
			result.add((Message) message);
		}
		return result;
	}

	public boolean saveMessage(Message message) {
		Object response = null;
		response = entityManager.merge(message);
		if (response != null)
			return true;
		return false;
	}
}
