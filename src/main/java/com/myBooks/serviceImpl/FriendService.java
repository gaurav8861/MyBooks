package com.myBooks.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myBooks.DAO.IFriendDAO;
import com.myBooks.DAO.ILoginDAO;
import com.myBooks.Exception.BookException;
import com.myBooks.constants.CONSTANTS;
import com.myBooks.model.VO.SendReqVO;
import com.myBooks.model.VO.UserVOReq;
import com.myBooks.model.VO.UserVOResponse;
import com.myBooks.model.VO.UserVOResponsePending;
import com.myBooks.model.dto.EmailDTO;
import com.myBooks.model.dto.PhoneDTO;
import com.myBooks.model.dto.ProfileDTO;
import com.myBooks.model.dto.SendReqDTO;
import com.myBooks.service.IFriendService;
import com.myBooks.util.DateConversion;

import oracle.jrockit.jfr.tools.ConCatRepository;

@Service
public class FriendService implements IFriendService{

	@Autowired
	private IFriendDAO friendDAO;
	
	@Autowired
	private ILoginDAO loginDAO;
	
	@Override
	public List<UserVOResponse> listAllUsers(UserVOReq userreq) throws BookException {
		List<UserVOResponse> userVOResponses = new ArrayList<>();
			ProfileDTO profile = loginDAO.validateAccessToken(String.valueOf(userreq.getProfileId()),userreq.getAccessToken());
			if(profile == null){
				throw new BookException(CONSTANTS.SESSION_EXPIRED,CONSTANTS.SESSION_EXPIRED_CODE);
			}
			List<ProfileDTO> listOfUsers = friendDAO.listAllUsres();
			for (ProfileDTO profileDTO : listOfUsers) {
				UserVOResponse userVOResponse = new UserVOResponse();
				Mapper mapper = new DozerBeanMapper();
				mapper.map(profileDTO, userVOResponse);
				List<EmailDTO> listOfEmails = profileDTO.getEmailList();
				for (EmailDTO emailDTO : listOfEmails) {
					if(emailDTO.getEmail() != null && emailDTO.getEmailType().equalsIgnoreCase(CONSTANTS.PRIMARY))
						userVOResponse.setEmail(emailDTO.getEmail());
				}
				List<PhoneDTO> listOfPhones = profileDTO.getPhoneList();
				for (PhoneDTO phoneDTO : listOfPhones) {
					if(phoneDTO.getPhoneNumber() != null && phoneDTO.getPhoneType().equalsIgnoreCase(CONSTANTS.PRIMARY))
						userVOResponse.setPhoneNumber(phoneDTO.getPhoneNumber());
				}
				if(!userVOResponse.getProfileId().equals(userreq.getProfileId())){
					userVOResponses.add(userVOResponse);
				}
			}
		return userVOResponses;
	}

	@Override
	public void sendFriendRequest(SendReqVO sendReqVO) throws BookException {
		loginDAO.validateAccessToken(String.valueOf(sendReqVO.getProfileId()),sendReqVO.getAccessToken());
		sendReqVO.setFriendProfileId(sendReqVO.getFriendProfileId());
		sendReqVO.setStatus(CONSTANTS.UNAPPROVED);
		sendReqVO.setRequestedDate(DateConversion.DateToString(new Date()));
		sendReqVO.setLoggedInProfileId(String.valueOf(sendReqVO.getProfileId()));
		Mapper mapper = new DozerBeanMapper();
		SendReqDTO sendReqDTO = new SendReqDTO();
		mapper.map(sendReqVO, sendReqDTO);
		friendDAO.sendFriendRequest(sendReqDTO);
	}

	@Override
	public void acceptFriendRequest(SendReqVO sendReqVO) throws BookException {
		loginDAO.validateAccessToken(String.valueOf(sendReqVO.getProfileId()),sendReqVO.getAccessToken());
		sendReqVO.setStatus(CONSTANTS.APPROVED);
		sendReqVO.setAcceptedDate(DateConversion.DateToString(new Date()));
		sendReqVO.setLoggedInProfileId(String.valueOf(sendReqVO.getProfileId()));
		Mapper mapper = new DozerBeanMapper();
		SendReqDTO sendReqDTO = new SendReqDTO();
		mapper.map(sendReqVO, sendReqDTO);
		friendDAO.updateFriend(sendReqDTO);
	}

	@Override
	public List<UserVOResponsePending> listOfFriendRequestPending(SendReqVO sendReqVO) throws BookException {
		ProfileDTO profile = loginDAO.validateAccessToken(String.valueOf(sendReqVO.getProfileId()),sendReqVO.getAccessToken());
		if(profile == null)
			throw new BookException(CONSTANTS.SESSION_EXPIRED,CONSTANTS.SESSION_EXPIRED_CODE);
		List<UserVOResponsePending> userVOResponsePendings = new ArrayList<>();
		UserVOResponsePending userVOResponsePending = null;
		List<SendReqDTO> listOfProfileFriend = friendDAO.listOfFriendRequestPending(sendReqVO);
		if(listOfProfileFriend != null){
			for (SendReqDTO sendReqDTO : listOfProfileFriend) {
				userVOResponsePending = new UserVOResponsePending();
				ProfileDTO profileDTO = loginDAO.getProfileById(String.valueOf(sendReqDTO.getLoggedInProfileId()));
				Mapper mapper = new DozerBeanMapper();
				mapper.map(profileDTO, userVOResponsePending);
				userVOResponsePending.setRequestId(sendReqDTO.getRequestId());
				for (EmailDTO email : profileDTO.getEmailList()) {
					if(email.getEmail().equalsIgnoreCase(CONSTANTS.PRIMARY))
						userVOResponsePending.setEmail(email.getEmail());
				}
				for (PhoneDTO phone : profileDTO.getPhoneList()) {
					if(phone.getPhoneNumber().equalsIgnoreCase(CONSTANTS.PRIMARY))
						userVOResponsePending.setPhoneNumber(phone.getPhoneNumber());
				}
				userVOResponsePendings.add(userVOResponsePending);
			}
		}
		return userVOResponsePendings;
	}

}
