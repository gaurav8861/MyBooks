package com.myBooks.model.dto;

import java.util.Date;
import java.util.List;

import com.myBooks.model.VO.BaseRequest;

public class ProfileDTO extends BaseRequest{
	
	private String firstName;
	private String lastName;
	private List<EmailDTO> emailList;
	private List<PhoneDTO> phoneList;
	private List<AddressDTO> addressList;
	private String imageLogo;
	private String dateOfBirth;
	private String dateOfJoining;
	private String gender;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<EmailDTO> getEmailList() {
		return emailList;
	}
	public void setEmailList(List<EmailDTO> emailList) {
		this.emailList = emailList;
	}
	public List<PhoneDTO> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(List<PhoneDTO> phoneList) {
		this.phoneList = phoneList;
	}
	public List<AddressDTO> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<AddressDTO> addressList) {
		this.addressList = addressList;
	}
	public String getImageLogo() {
		return imageLogo;
	}
	public void setImageLogo(String imageLogo) {
		this.imageLogo = imageLogo;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
}
