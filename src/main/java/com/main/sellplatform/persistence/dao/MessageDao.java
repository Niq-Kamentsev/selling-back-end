package com.main.sellplatform.persistence.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.main.sellplatform.entitymanager.analyzer.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.EntityManager;
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
        try {
            String sql = "SELECT DISTINCT reference as id FROM objreference WHERE (object_id IN (\n"
                    + "SELECT OBJECT_ID FROM objreference\n WHERE ATTR_ID = 29 AND reference = " + userId
                    + ") AND ATTR_ID = 28) OR (object_id IN (\n" + "SELECT OBJECT_ID FROM objreference\n"
                    + "WHERE ATTR_ID = 28 AND reference = " + userId + ") AND ATTR_ID = 29)";
            objects = entityManager.getObjectsByIdSeq(User.class, sql, null); // null
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
        String sql = "(OBJ_1ID_REF29  = " + currentUserId + " AND OBJ_1ID_REF28 = " + targetUser
                + ") OR (OBJ_1ID_REF29  = " + targetUser + " AND OBJ_1ID_REF28 = " + currentUserId
                + ") ORDER BY OBJ_5ATTR_31 ASC";
        Object[] messages = entityManager.getAllObjects(Message.class, sql, null); // null
        List<Message> result = new ArrayList<>();
        for (Object message : messages) {
            result.add((Message) message);
        }
        return result;
    }

    public List<Message> getMessages(Long currentUserId, Long targetUser, Long lotId) {
        List<Object> statements = new ArrayList<>();
        statements.add(currentUserId);
        statements.add(targetUser);
        statements.add(targetUser);
        statements.add(currentUserId);
        statements.add(lotId);
        String sql = queries.whereMessageLots();
        Object[] messages = entityManager.getAllObjects(Message.class, sql, statements);
        if (messages == null) return null;
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
