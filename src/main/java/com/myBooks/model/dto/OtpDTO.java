package com.myBooks.model.dto;

import java.util.Date;

public class OtpDTO {
	
	private Integer otpId;
	private String otp;
	private String otpDate;
	private String validUpto;
	private Integer profileId;
	public Integer getOtpId() {
		return otpId;
	}
	public void setOtpId(Integer otpId) {
		this.otpId = otpId;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	public String getOtpDate() {
		return otpDate;
	}
	public void setOtpDate(String otpDate) {
		this.otpDate = otpDate;
	}
	public String getValidUpto() {
		return validUpto;
	}
	public void setValidUpto(String validUpto) {
		this.validUpto = validUpto;
	}
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	
	

}
