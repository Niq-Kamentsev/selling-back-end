package com.main.sellplatform.entitymanager.analyzer;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class Queries {
    final String filePath = "src/main/resources/queries.properties";

    private String get(String name) {
        try (InputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(name);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String test() {
        return get("test");
    }

    public String whereByUserEmail() {
        return get("user.where.email");
    }

    public String whereByUserId() {
        return get("user.where.id");
    }

    public String whereByLotOwnerId() {
        return get("lot.where.ownerId");
    }

    public String whereBuyableLot() {
        return get("lot.where.buyable");
    }

    public String orderBy() {
        return get("common.orderBy");
    }

    public String selectAllUserId() {
        return get("user.select.allId");
    }

    public String selectMessageChannel() {
        return get("message.select.channels");
    }

    public String whereMessageMessages() {
        return get("message.where.messages");
    }
    
    public String whereMessageNewMessages() {
    	return get("message.where.newMessages");
    }

    public String whereMessageLots() {
        return get("message.where.messagesLots");
    }

    public String deleteReferences() {
        return get("common.deleteReference");
    }

    public String deleteAttributes() {
        return get("common.deleteAttribute");
    }

    public String deleteObjects() {
        return get("common.deleteObject");
    }

    public String deleteChilds() {
        return get("common.deleteParent");
    }

    public String getChilds() {
        return get("common.getChilds");
    }

    public String whereByFinBid() {
        return get("bid.where.finBid");
    }

    public String whereByLastBid() {
        return get("bid.where.lastBid");
    }
}
