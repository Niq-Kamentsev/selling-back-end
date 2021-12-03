package com.main.sellplatform.entitymanager.testobj;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;

@Objtype(5)
public class Message extends GeneralObject {
    @Reference(attributeId = 28)
    User sender;
    @Reference(attributeId = 29)
    User receiver;
    @Attribute(attrTypeId = 30)
    Long id;
    @Attribute(attrTypeId = 31)
    String date;
    @Attribute(attrTypeId = 32)
    String msg;
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
}
