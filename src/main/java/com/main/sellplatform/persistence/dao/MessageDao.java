package com.main.sellplatform.persistence.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;
import com.main.sellplatform.entitymanager.testobj.Message;
import com.main.sellplatform.entitymanager.testobj.User;
import com.main.sellplatform.persistence.entity.MessageChannel;

@Component
public class MessageDao {
	private final EntityManager entityManager;

	@Autowired
	public MessageDao(EntityManager entityManager) {
		this.entityManager = entityManager;
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
		Set<Long> resultSet = new HashSet<>();
		List<MessageChannel> result = new ArrayList<>();
		try {
			String where = "OBJ_" + User.class.getAnnotation(Objtype.class).value() + "ID" + "_REF"
					+ Message.class.getDeclaredField("sender").getAnnotation(Reference.class).attributeId() + " = "
					+ userId;
			resultSet.addAll(entityManager.getDistinctReferences(Message.class, where,
					Message.class.getDeclaredField("receiver").getAnnotation(Reference.class),
					User.class.getAnnotation(Objtype.class)));
			where = "OBJ_" + User.class.getAnnotation(Objtype.class).value() + "ID" + "_REF"
					+ Message.class.getDeclaredField("receiver").getAnnotation(Reference.class).attributeId() + " = "
					+ userId;
			resultSet.addAll(entityManager.getDistinctReferences(Message.class, where,
					Message.class.getDeclaredField("sender").getAnnotation(Reference.class),
					User.class.getAnnotation(Objtype.class)));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		for (Long targetId : resultSet) {
			result.add(new MessageChannel(targetId));
		}
		return result;
	}

	public List<Message> getMessages(Long currentUserId, Long targetUser) {
		List<Message> result = new ArrayList<>();
		String where = "OBJ_" ;
		return null;
	}
}
