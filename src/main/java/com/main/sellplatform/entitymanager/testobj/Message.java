package com.main.sellplatform.entitymanager.testobj;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;

import java.util.Date;

@Objtype(5)
public class Message extends GeneralObject {
    @Reference(attributeId = 28)
    User sender;
    @Reference(attributeId = 29)
    User receiver;
    @Reference(attributeId = 66)
    Lot lot;
    @Attribute(attrTypeId = 30)
    Integer id;
    @Attribute(attrTypeId = 31, type = Attribute.ValueType.DATE_VALUE)
    Date date;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Message{" +
				"sender=" + sender +
				", receiver=" + receiver +
				", id=" + id +
				", date=" + date +
				", msg='" + msg + '\'' +
				'}';
	}
}
