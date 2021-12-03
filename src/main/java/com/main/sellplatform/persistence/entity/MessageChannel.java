package com.main.sellplatform.persistence.entity;

public class MessageChannel {
	private Long targetUser;

	public MessageChannel() {

	}

	public MessageChannel(Long targetUser) {
		this.targetUser = targetUser;
	}

	public Long getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(Long targetUser) {
		this.targetUser = targetUser;
	}

}
