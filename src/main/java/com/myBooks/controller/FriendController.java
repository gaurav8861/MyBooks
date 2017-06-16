package com.myBooks.controller;

import java.util.HashMap;
import java.util.List;
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
import com.myBooks.model.VO.SendReqVO;
import com.myBooks.model.VO.UserVOReq;
import com.myBooks.model.VO.UserVOResponse;
import com.myBooks.model.VO.UserVOResponsePending;
import com.myBooks.service.IFriendService;

@RestController
@RequestMapping("/friend")
public class FriendController {

	private final static Logger LOGGER = Logger.getLogger(FriendController.class);
	
	@Autowired
	private IFriendService friendService;

	@RequestMapping(value="/listAllUsers",method=RequestMethod.POST)
		public @ResponseBody Map<String,Object> listAllUsers(@RequestBody UserVOReq userreq){
			Map<String,Object> responseMap = new HashMap<String,Object>();
			try {
				List<UserVOResponse> responseUser = friendService.listAllUsers(userreq);
				responseMap.put(CONSTANTS.OBJECT,responseUser);
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
	@RequestMapping(value="/sendFriendRequest",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> sendFriendRequest(@RequestBody SendReqVO sendReqVO){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			friendService.sendFriendRequest(sendReqVO);
			responseMap.put(CONSTANTS.MESSAGE,CONSTANTS.FRIEND_REQUEST_SEND_SUCCESSFULLY);
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
	@RequestMapping(value="/listOfFriendRequest",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> listOfFriendRequest(@RequestBody SendReqVO sendReqVO){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			List<UserVOResponsePending> listOfFriendRequest = friendService.listOfFriendRequestPending(sendReqVO);
			responseMap.put(CONSTANTS.OBJECT,listOfFriendRequest);
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
	@RequestMapping(value="/acceptFriendRequest",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> acceptFriendRequest(@RequestBody SendReqVO sendReqVO){
		Map<String,Object> responseMap = new HashMap<String,Object>();
		try {
			friendService.acceptFriendRequest(sendReqVO);
			responseMap.put(CONSTANTS.MESSAGE,CONSTANTS.FRIEND_REQUEST_ACCEPTED);
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
}
