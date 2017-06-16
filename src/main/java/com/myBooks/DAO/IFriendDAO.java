package com.myBooks.DAO;

import java.util.List;

import com.myBooks.Exception.BookException;
import com.myBooks.model.VO.SendReqVO;
import com.myBooks.model.dto.ProfileDTO;
import com.myBooks.model.dto.SendReqDTO;

public interface IFriendDAO {

	public List<ProfileDTO> listAllUsres() throws BookException;

	public void sendFriendRequest(SendReqDTO sendReqVO)throws BookException;

	public void updateFriend(SendReqDTO sendReqDTO) throws BookException;

	public List<SendReqDTO> listOfFriendRequestPending(SendReqVO sendReqVO) throws BookException;

}
