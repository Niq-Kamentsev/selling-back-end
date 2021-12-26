package com.main.sellplatform.controller.dto.messagedto;

public class MessageDTO {

	private Long sender;
	private Long receiver;
	private String message;
	private Long lot;

	public Long getSender() {
		return sender;
	}

	public void setSender(Long sender) {
		this.sender = sender;
	}

	public Long getReceiver() {
		return receiver;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getLot() {
		return lot;
	}

	public void setLot(Long lot) {
		this.lot = lot;
	}
}
