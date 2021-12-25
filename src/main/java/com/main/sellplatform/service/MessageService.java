package com.main.sellplatform.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.sellplatform.controller.dto.messagedto.MessageDTO;
import com.main.sellplatform.entitymanager.testdao.LotDao2;
import com.main.sellplatform.entitymanager.testobj.Bid;
import com.main.sellplatform.entitymanager.testobj.Lot;
import com.main.sellplatform.persistence.dao.BidDao;
import com.main.sellplatform.persistence.dao.MessageDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Message;
import com.main.sellplatform.persistence.entity.MessageChannel;
import com.main.sellplatform.persistence.entity.User;

@Service
public class MessageService {
	private final MessageDao messageDao;
	private final UserDao userDao;
	private final BidDao bidDao;
	private final LotDao2 lotDao;

	@Autowired
	public MessageService(MessageDao messageDao, UserDao userDao, BidDao bidDao, LotDao2 lotDao) {
		this.messageDao = messageDao;
		this.userDao = userDao;
		this.bidDao = bidDao;
		this.lotDao = lotDao;
	}

	public List<MessageChannel> getChannels(Long userId) {
		return messageDao.getChannels(userId);
	}

	public List<Message> getMessages(Long currentUserId, Long targetUser, Long bidId) {
		List<Message> result = new ArrayList<>();
		Bid bid = bidDao.getBidById(bidId);
		for (com.main.sellplatform.entitymanager.testobj.Message msg : messageDao.getMessages(currentUserId, targetUser,
				bid.getLot().getId())) {
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
		Bid bid = bidDao.getBidById(message.getBidId());
		Lot lot = lotDao.getLotById(bid.getLot().getId(), null);
		User userSender = userDao.getUser(senderUserId);
		User userReceiver = userDao.getUser(message.getReceiver());
		if (bid.getUser().getId() != userReceiver.getId() && lot.getUser().getId() != userReceiver.getId()) {
			throw new IllegalArgumentException();
		}
		com.main.sellplatform.entitymanager.testobj.Message result = new com.main.sellplatform.entitymanager.testobj.Message();
		result.setSender(userSender);
		result.setReceiver(userReceiver);
		result.setLot(lot);
		result.setMsg(message.getMessage());
		result.setDate(Calendar.getInstance().getTime());
		result.setName("Message");
		result.setDescr("Message");
		return messageDao.saveMessage(result);
	}
}
