package com.main.sellplatform.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.main.sellplatform.controller.dto.messagedto.MessageDTO;
import com.main.sellplatform.entitymanager.testdao.LotDao2;
import com.main.sellplatform.entitymanager.testobj.Bid;
import com.main.sellplatform.entitymanager.testobj.Lot;
import com.main.sellplatform.persistence.dao.BidDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.sellplatform.persistence.dao.MessageDao;
import com.main.sellplatform.persistence.entity.Message;
import com.main.sellplatform.persistence.entity.MessageChannel;

@Service
public class MessageService {
    private final MessageDao messageDao;
    private final UserDao userDao;
    private final LotDao2 lotDao2;
    private final BidService bidService;
    private final EmitterService emitterService;
    private final BidDao bidDao;

    @Autowired
    public MessageService(MessageDao messageDao, UserDao userDao, LotDao2 lotDao2, BidService bidService,
                          EmitterService emitterService, BidDao bidDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
        this.lotDao2 = lotDao2;
        this.bidService = bidService;
        this.emitterService = emitterService;
        this.bidDao = bidDao;
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

    public List<Message> getMessages(Long currentUserId, Long targetUser) {
        List<Message> result = new ArrayList<>();
        for (com.main.sellplatform.entitymanager.testobj.Message msg : messageDao.getMessages(currentUserId, targetUser)) {
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

    public com.main.sellplatform.entitymanager.testobj.Message saveLotMessage(MessageDTO message, User userByEmail) {
        com.main.sellplatform.entitymanager.testobj.Message result = new com.main.sellplatform.entitymanager.testobj.Message();
        result.setSender(userByEmail);
        Bid bid = bidService.getFinalBidByLot(message.getLot());
        if (bid == null) return null;
        Lot lot = lotDao2.getLotById(bid.getLot().getId(), null);
        if (!userByEmail.equals(lot.getUser())) return null;
        List<com.main.sellplatform.entitymanager.testobj.Message> exists = messageDao.getMessages(lot.getUser().getId(),
                bid.getUser().getId(), bid.getLot().getId());
        if (!(exists == null || exists.size() == 0)) return result;

        result.setLot(lot);
        result.setReceiver(bid.getUser());
        result.setMsg("Hello, " + bid.getUser().getFirstName() + "! You have won the lot \"" + lot.getName() + "\" with price "
                + bid.getPrice() + ". Please confirm your order.");
        result.setDate(Calendar.getInstance().getTime());

        com.main.sellplatform.entitymanager.testobj.Message res = messageDao.saveLotMessage(result);
        emitterService.pushNotification(bid.getUser().getId(), lot.getUser().getId());
        return res;
    }
}
