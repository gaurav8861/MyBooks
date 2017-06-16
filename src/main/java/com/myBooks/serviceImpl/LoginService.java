package com.myBooks.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myBooks.DAO.ILoginDAO;
import com.myBooks.Exception.BookException;
import com.myBooks.constants.CONSTANTS;
import com.myBooks.model.VO.ChangePasswordRes;
import com.myBooks.model.VO.OtpReq;
import com.myBooks.model.VO.ProfileVO;
import com.myBooks.model.dto.AddressDTO;
import com.myBooks.model.dto.EmailDTO;
import com.myBooks.model.dto.OtpDTO;
import com.myBooks.model.dto.PhoneDTO;
import com.myBooks.model.dto.ProfileDTO;
import com.myBooks.service.ILoginService;
import com.myBooks.util.DateConversion;
import com.myBooks.util.GenerateAccessToken;

@Service
public class LoginService implements ILoginService {

	@Autowired
	private ILoginDAO loginDAO;

	public ProfileDTO signUp(ProfileVO profileVO) throws BookException {
		Boolean isExists = loginDAO.validate(profileVO);
		System.out.println("isExists : " + isExists);
		if (!isExists) {
			Mapper mapper = new DozerBeanMapper();
			ProfileDTO profileDTO = new ProfileDTO();
			mapper.map(profileVO, profileDTO);
			profileDTO.setPassword("password");
			List<AddressDTO> addressList = new ArrayList<AddressDTO>();
			List<PhoneDTO> phoneList = new ArrayList<PhoneDTO>();
			List<EmailDTO> emailList = new ArrayList<EmailDTO>();
			addressList.add(profileVO.getAddress());
			PhoneDTO phonedto = new PhoneDTO();
			phonedto.setPhoneNumber(profileVO.getPhoneNumber());
			phonedto.setPhoneType(CONSTANTS.PRIMARY);
			phoneList.add(phonedto);
			EmailDTO emaildto = new EmailDTO();
			emaildto.setEmail(profileVO.getEmail());
			emaildto.setEmailType(CONSTANTS.PRIMARY);
			emailList.add(emaildto);
			profileDTO.setAddressList(addressList);
			profileDTO.setEmailList(emailList);
			profileDTO.setPhoneList(phoneList);
			ProfileDTO responseUser = loginDAO.signUp(profileDTO);
			return responseUser;
		} else {
			throw new BookException(CONSTANTS.EMAIL_PHONE_ALREADY_EXISTS, CONSTANTS.EMAIL_PHONE_ALREADY_EXISTS_CODE);
		}
	}

	public ProfileDTO login(ProfileVO profileVO) throws BookException {
		ProfileDTO responseProfile = loginDAO.validateLogin(profileVO);
		if (responseProfile != null) {
			String accessToken = GenerateAccessToken.generateAppAccessToken();
			responseProfile.setAccessToken(accessToken);
			updateAccessToken(responseProfile.getProfileId(), accessToken);
			return responseProfile;
		} else {
			throw new BookException(CONSTANTS.USER_NOT_EXISTS_WITH_THE_GIVEN_EMAIL,
					CONSTANTS.USER_NOT_EXISTS_WITH_THE_GIVEN_EMAIL_CODE);
		}
	}

	private void updateAccessToken(Integer profileId, String accessToken) throws BookException {
		loginDAO.updateAccessToken(profileId, accessToken);

	}

	private Boolean validateUser(ProfileVO profileVO) throws BookException {
		Boolean isExists = loginDAO.validate(profileVO);
		return isExists;
	}

	@Override
	public void changePassword(ChangePasswordRes changePasswordRes) throws BookException {
		if (changePasswordRes.getProfileId() != null && changePasswordRes.getAccessToken() != null) {
			ProfileDTO profile  = loginDAO.validateAccessToken(String.valueOf(changePasswordRes.getProfileId()),changePasswordRes.getAccessToken());
			if(profile == null)
				throw new BookException(CONSTANTS.SESSION_EXPIRED,CONSTANTS.SESSION_EXPIRED_CODE);
			Boolean oldPasswordExists = loginDAO.changePassword(changePasswordRes);
			if (!oldPasswordExists)
				throw new BookException(CONSTANTS.OLD_PASSWORD_DONOT_MATCH, CONSTANTS.OLD_PASSWORD_DONOT_MATCH_CODE);
		} else {
			throw new BookException(CONSTANTS.PROFILEID_ACCESSTOKEN_IS_NULL,CONSTANTS.PROFILEID_ACCESSTOKEN_IS_NULL_CODE);
		}
	}

	@Override
	public void forgotPassword(ProfileVO profileVO) throws BookException {
		if (profileVO.getEmail() != null || profileVO.getPhoneNumber() != null) {
			Boolean isExists = loginDAO.validate(profileVO);
			if (isExists) {
				ProfileDTO profile = loginDAO.getProfileByEmailorPhone(profileVO);
				if (profile != null) {
					OtpDTO otpDTO = new OtpDTO();
					otpDTO.setOtp("ABCD");
					otpDTO.setOtpDate(DateConversion.DateToString(new Date()));
					otpDTO.setProfileId(profile.getProfileId());
					otpDTO.setValidUpto(
							DateConversion.DateToString(new Date(System.currentTimeMillis() + 5 * 60 * 1000)));
					loginDAO.updateOtp(otpDTO);
				} else {
					throw new BookException(CONSTANTS.EMAIL_PHONE_IS_NOT_FOUND,CONSTANTS.EMAIL_PHONE_IS_NOT_FOUND_CODE);
				}
			} else {
				throw new BookException(CONSTANTS.EMAIL_PHONE_IS_NOT_FOUND, CONSTANTS.EMAIL_PHONE_IS_NOT_FOUND_CODE);
			}
		} else {
			throw new BookException(CONSTANTS.EMAIL_PHONE_IS_NULL, CONSTANTS.EMAIL_PHONE_IS_NULL_CODE);
		}

	}

	@Override
	public void validateForgotPassword(OtpReq otpreq) throws BookException {
		if (otpreq.getEmail() != null || otpreq.getPhoneNumber() != null) {
			if (otpreq.getOtp() != null && otpreq.getNewPassword() != null) {
				ProfileVO profileVO = new ProfileVO();
				profileVO.setEmail(otpreq.getEmail());
				profileVO.setPassword(otpreq.getNewPassword());
				ProfileDTO profile = loginDAO.getProfileByEmailorPhone(profileVO);
				OtpDTO otpDTO = loginDAO.validateForgotPassword(profile);
				if (otpDTO != null) {
					String currentDate = DateConversion.DateToString(new Date());
					String otpValidateDate = otpDTO.getValidUpto();
					long diff = DateConversion.DateDifferent(currentDate, otpValidateDate);
					System.out.println("Diff : " + diff);
					if (diff != 0l) {
						loginDAO.updatePassword(profile.getProfileId(), otpreq.getNewPassword());
						System.out.println("Bingo");
					} else {
						throw new BookException(CONSTANTS.OTP_EXPIRED, CONSTANTS.OTP_EXPIRED_CODE);
					}
				} else {
					throw new BookException(CONSTANTS.PLEASE_GENERATE_OTP, CONSTANTS.PLEASE_GENERATE_OTP_CODE);
				}
			} else {
				throw new BookException(CONSTANTS.OTP_PASSWORD_IS_NULL, CONSTANTS.OTP_PASSWORD_IS_NULL_CODE);
			}
		} else {
			throw new BookException(CONSTANTS.EMAIL_PHONE_IS_NULL, CONSTANTS.EMAIL_PHONE_IS_NULL_CODE);
		}
	}

}
