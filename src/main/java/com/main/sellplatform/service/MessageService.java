package com.main.sellplatform.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.main.sellplatform.persistence.entity.Lot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.sellplatform.controller.dto.messagedto.MessageDTO;
import com.main.sellplatform.entitymanager.testdao.LotDao2;
import com.main.sellplatform.entitymanager.testobj.Bid;
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
	private final BidService bidService;
	private final EmitterService emitterService;

	@Autowired
	public MessageService(MessageDao messageDao, UserDao userDao, BidDao bidDao, LotDao2 lotDao, BidService bidService, EmitterService emitterService) {
		this.messageDao = messageDao;
		this.userDao = userDao;
		this.bidDao = bidDao;
		this.lotDao = lotDao;
		this.bidService = bidService;
		this.emitterService = emitterService;

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
		if (bid.getUser().getId() != userReceiver.getId() && lot.getOwner().getId() != userReceiver.getId()) {
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



    public com.main.sellplatform.entitymanager.testobj.Message saveLotMessage(MessageDTO message, User userByEmail) {
        com.main.sellplatform.entitymanager.testobj.Message result = new com.main.sellplatform.entitymanager.testobj.Message();
        result.setSender(userByEmail);
        Bid bid = bidService.getFinalBidByLot(message.getLot());
        if (bid == null) return null;
        Lot lot = lotDao.getLotById(bid.getLot().getId(), null);
        if (!userByEmail.equals(lot.getOwner())) return null;
        List<com.main.sellplatform.entitymanager.testobj.Message> exists = messageDao.getMessages(lot.getOwner().getId(),
                bid.getUser().getId(), bid.getLot().getId());
        if (!(exists == null || exists.size() == 0)) return result;

        result.setLot(lot);
        result.setReceiver(bid.getUser());
        result.setMsg("Hello, " + bid.getUser().getFirstName() + "! You have won the lot \"" + lot.getName() + "\" with price "
                + bid.getPrice() + ". Please confirm your order.");
        result.setDate(Calendar.getInstance().getTime());

        com.main.sellplatform.entitymanager.testobj.Message res = messageDao.saveLotMessage(result);
        emitterService.pushNotification(bid.getUser().getId(), lot.getOwner().getId());
        return res;
    }
}
