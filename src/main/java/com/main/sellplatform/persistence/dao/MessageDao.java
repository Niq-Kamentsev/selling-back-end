package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.testobj.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDao {
    private final EntityManager entityManager;

    @Autowired
    public MessageDao(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public Message[] getAllMessages(){
        Object[] objects = entityManager.getAllObjects(Message.class);
        Message[] messages = new Message[objects.length];
        for(int i=0;i<objects.length;++i){
            messages[i]=(Message) objects[i];
        }
        return messages;
    }
}
