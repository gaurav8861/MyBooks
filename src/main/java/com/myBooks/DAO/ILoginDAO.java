package com.myBooks.DAO;

import com.myBooks.Exception.BookException;
import com.myBooks.model.VO.ChangePasswordRes;
import com.myBooks.model.VO.ProfileVO;
import com.myBooks.model.dto.OtpDTO;
import com.myBooks.model.dto.ProfileDTO;

public interface ILoginDAO {
	
	public ProfileDTO signUp(ProfileDTO profileDTO) throws BookException;

	public Boolean validate(ProfileVO profileVO) throws BookException;

	public ProfileDTO login(ProfileVO profileVO) throws BookException;

	public void updateAccessToken(Integer profileId, String accessToken) throws BookException;

	public ProfileDTO validateLogin(ProfileVO profileVO) throws BookException;

	public ProfileDTO validateAccessToken(String profileId, String accessToken)throws BookException;

	public Boolean changePassword(ChangePasswordRes changePasswordRes) throws BookException;

	public ProfileDTO getProfileByEmailorPhone(ProfileVO profileVO) throws BookException;

	public void updateOtp(OtpDTO otpDTO) throws BookException;

	public OtpDTO validateForgotPassword(ProfileDTO profile) throws BookException;

	public void updatePassword(Integer profileId, String string) throws BookException;

	public ProfileDTO getProfileById(String loggedInProfileId) throws BookException;


}
