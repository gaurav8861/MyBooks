package com.myBooks.model.VO;

public class SendReqVO extends BaseRequest{
	
	private String requestId;
	private String friendProfileId;
	private String requestedDate;
	private String status;
	private String acceptedDate;
	private String loggedInProfileId;

	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getFriendProfileId() {
		return friendProfileId;
	}

	public void setFriendProfileId(String friendProfileId) {
		this.friendProfileId = friendProfileId;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(String acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public String getLoggedInProfileId() {
		return loggedInProfileId;
	}

	public void setLoggedInProfileId(String loggedInProfileId) {
		this.loggedInProfileId = loggedInProfileId;
	}
	
	

}
