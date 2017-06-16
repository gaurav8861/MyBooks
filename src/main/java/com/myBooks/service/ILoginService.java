package com.myBooks.service;

import com.myBooks.Exception.BookException;
import com.myBooks.model.VO.ChangePasswordRes;
import com.myBooks.model.VO.OtpReq;
import com.myBooks.model.VO.ProfileVO;
import com.myBooks.model.dto.ProfileDTO;

public interface ILoginService {

	public ProfileDTO signUp(ProfileVO profileVO) throws BookException;

	public ProfileDTO login(ProfileVO profileVO) throws BookException;

	public void changePassword(ChangePasswordRes changePasswordRes) throws BookException;

	public void forgotPassword(ProfileVO profileVO) throws BookException;

	public void validateForgotPassword(OtpReq otpreq) throws BookException;

}
