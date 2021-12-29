package com.main.sellplatform.controller.dto.messagedto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MessageDTO {

	@NotNull
	private Long receiver;
	@NotNull
	private Long bidId;
	@NotEmpty
	private String message;

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

	public Long getBidId() {
		return bidId;
	}

	public void setBidId(Long bidId) {
		this.bidId = bidId;
	}

}
