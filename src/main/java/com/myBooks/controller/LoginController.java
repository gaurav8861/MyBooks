package com.myBooks.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.myBooks.Exception.BookException;
import com.myBooks.constants.CONSTANTS;
import com.myBooks.model.VO.ChangePasswordRes;
import com.myBooks.model.VO.OtpReq;
import com.myBooks.model.VO.ProfileVO;
import com.myBooks.model.dto.ProfileDTO;
import com.myBooks.service.ILoginService;



@RestController
@RequestMapping("/login")
public class LoginController {
	private final static Logger LOGGER = Logger.getLogger(LoginController.class);
	@Autowired
	private ILoginService loginService;

	@RequestMapping(value="/signup",method=RequestMethod.POST)
		public @ResponseBody Map<String,Object> signUp(@RequestBody ProfileVO profileVO){
			Map<String,Object> responseMap = new HashMap<String,Object>();
			try {
				ProfileDTO responseUser = loginService.signUp(profileVO);
				//responseMap.put(CONSTANTS.OBJECT,responseUser);
				responseMap.put(CONSTANTS.STATUS,CONSTANTS.SUCCESS);
				responseMap.put(CONSTANTS.STATUS_CODE,CONSTANTS.SUCCESS_CODE);
			}catch(BookException e){
				e.printStackTrace();
				LOGGER.error("Book Exeption : ",e);
				responseMap.put(CONSTANTS.STATUS, e.getMessage());
				responseMap.put(CONSTANTS.STATUS_CODE, e.getErrorCode());
			}catch (Exception e) {
				LOGGER.error("Generic Exeption : ",e);
				e.printStackTrace();
				responseMap.put(CONSTANTS.MESSAGE, CONSTANTS.UNEXPECTED_ERROR_MESSAGE);
				responseMap.put(CONSTANTS.ERROR_CODE, CONSTANTS.UNEXPECTED_ERROR_CODE);
			}
			return responseMap;
		}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> login(@RequestBody ProfileVO profileVO){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			ProfileDTO responseUser = loginService.login(profileVO);
			responseMap.put(CONSTANTS.OBJECT,responseUser);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.SUCCESS);
			responseMap.put(CONSTANTS.STATUS_CODE,CONSTANTS.SUCCESS_CODE);
		}catch(BookException e){
			e.printStackTrace();
			LOGGER.error("Book Exeption : ",e);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, e.getMessage());
			responseMap.put(CONSTANTS.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			LOGGER.error("Generic Exeption : ",e);
			e.printStackTrace();
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, CONSTANTS.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(CONSTANTS.ERROR_CODE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> changePassword(@RequestBody ChangePasswordRes changePasswordRes){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			loginService.changePassword(changePasswordRes);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.SUCCESS);
			responseMap.put(CONSTANTS.MESSAGE,CONSTANTS.CHANGE_PASSWORD_SUCCESSFUL);
			responseMap.put(CONSTANTS.STATUS_CODE,CONSTANTS.SUCCESS_CODE);
		}catch(BookException e){
			e.printStackTrace();
			LOGGER.error("Book Exeption : ",e);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, e.getMessage());
			responseMap.put(CONSTANTS.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			LOGGER.error("Generic Exeption : ",e);
			e.printStackTrace();
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, CONSTANTS.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(CONSTANTS.ERROR_CODE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	@RequestMapping(value="/forgotPassword",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> forgotPassword(@RequestBody ProfileVO profileVO){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			loginService.forgotPassword(profileVO);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.SUCCESS);
			responseMap.put(CONSTANTS.MESSAGE, CONSTANTS.PASSWORD_SEND);
			responseMap.put(CONSTANTS.STATUS_CODE,CONSTANTS.SUCCESS_CODE);
		}catch(BookException e){
			e.printStackTrace();
			LOGGER.error("Book Exeption : ",e);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, e.getMessage());
			responseMap.put(CONSTANTS.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			LOGGER.error("Generic Exeption : ",e);
			e.printStackTrace();
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, CONSTANTS.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(CONSTANTS.ERROR_CODE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}
	@RequestMapping(value="/validateForgotPassword",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> validateForgotPassword(@RequestBody OtpReq otpreq){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			loginService.validateForgotPassword(otpreq);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.SUCCESS);
			responseMap.put(CONSTANTS.MESSAGE, CONSTANTS.PASSWORD_CHANGED_SUCCESSFULLY);
			responseMap.put(CONSTANTS.STATUS_CODE,CONSTANTS.SUCCESS_CODE);
		}catch(BookException e){
			e.printStackTrace();
			LOGGER.error("Book Exeption : ",e);
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, e.getMessage());
			responseMap.put(CONSTANTS.ERROR_CODE, e.getErrorCode());
		}catch (Exception e) {
			LOGGER.error("Generic Exeption : ",e);
			e.printStackTrace();
			responseMap.put(CONSTANTS.STATUS,CONSTANTS.FAIL);
			responseMap.put(CONSTANTS.MESSAGE, CONSTANTS.UNEXPECTED_ERROR_MESSAGE);
			responseMap.put(CONSTANTS.ERROR_CODE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}
		return responseMap;
	}

}
