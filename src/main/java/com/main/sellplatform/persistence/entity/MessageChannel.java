package com.main.sellplatform.persistence.entity;

public class MessageChannel {

	public MessageChannel() {

	}

	public MessageChannel(Long targetUser) {
		this.targetUserId = targetUser;
	}

	private Long bidId;
	private Long targetUserId;
	private String username;

	public Long getBidId() {
		return bidId;
	}

	public void setBidId(Long bidId) {
		this.bidId = bidId;
	}

	public Long getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(Long targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "MessageChannel{" +
				"bidId=" + bidId +
				", targetUserId=" + targetUserId +
				", username='" + username + '\'' +
				'}';
	}
}
