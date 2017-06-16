package com.myBooks.service;

import java.util.List;

import com.myBooks.Exception.BookException;
import com.myBooks.model.VO.SendReqVO;
import com.myBooks.model.VO.UserVOReq;
import com.myBooks.model.VO.UserVOResponse;
import com.myBooks.model.VO.UserVOResponsePending;

public interface IFriendService {

	public List<UserVOResponse> listAllUsers(UserVOReq userreq) throws BookException;

	public void sendFriendRequest(SendReqVO sendReqVO)throws BookException;

	public void acceptFriendRequest(SendReqVO sendReqVO) throws BookException;

	public List<UserVOResponsePending> listOfFriendRequestPending(SendReqVO sendReqVO) throws BookException;

}
