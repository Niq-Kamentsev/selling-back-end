package com.main.sellplatform.persistence.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.main.sellplatform.persistence.entity.Lot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.analyzer.Queries;
import com.main.sellplatform.entitymanager.testdao.LotDao2;
import com.main.sellplatform.entitymanager.testobj.Bid;

import com.main.sellplatform.entitymanager.testobj.Message;
import com.main.sellplatform.persistence.entity.MessageChannel;

@Component
public class MessageDao {
	private final EntityManager entityManager;
	private final Queries queries;
	private final LotDao2 lotDao;

	@Autowired
	public MessageDao(EntityManager entityManager, Queries queries, LotDao2 lotDao) {
		this.entityManager = entityManager;
		this.queries = queries;
		this.lotDao = lotDao;
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
		statements.add("WON");
		statements.add(userId);
		statements.add(userId);
		try {
			objects = entityManager.getObjectsByIdSeq(Bid.class, queries.selectMessageChannel(), statements);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Object targetObject : objects) {
			Bid bid = (Bid) targetObject;
			MessageChannel channel = new MessageChannel();
			channel.setBidId(bid.getId());
			channel.setLotName(bid.getLot().getName());
			if (bid.getUser().getId() != userId) {
				channel.setTargetUserId(bid.getUser().getId());
				channel.setUsername(bid.getUser().getFirstName());
			} else {
				Lot fullLot = lotDao.getLotById(bid.getLot().getId(), null);
				channel.setTargetUserId(fullLot.getOwner().getId());
				channel.setUsername(fullLot.getOwner().getFirstName());
			}
			result.add(channel);
		}
		return result;
	}

	public List<Message> getMessages(Long currentUserId, Long targetUser, Long targetLotId, Long lastMessageId) {
		List<Message> messages = this.getPageOfMessages(currentUserId, targetUser, targetLotId, lastMessageId, null, null);
		Collections.reverse(messages);
		return messages;
	}

	public List<Message> getPageOfMessages(Long currentUserId, Long targetUser, Long targetLotId, Long lastMessageId,
			Long offset, Long nextRowsCount) {
		List<Object> statements = new ArrayList<>();
		String sql = null;
		if (lastMessageId != null) {
			sql = queries.whereMessageNewMessages();
			statements.add(lastMessageId);
		} else {
			sql = queries.whereMessageMessages();
		}
		statements.add(targetLotId);
		statements.add(currentUserId);
		statements.add(targetUser);
		statements.add(targetUser);
		statements.add(currentUserId);
		statements.add(currentUserId);
		statements.add(targetUser);
		Object[] messages = entityManager.getPageOfObjects(Message.class, sql, statements, offset, nextRowsCount);
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

	public Message saveLotMessage(Message message) {
		return entityManager.merge(message);
	}
}
