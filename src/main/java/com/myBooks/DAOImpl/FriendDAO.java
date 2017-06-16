package com.myBooks.DAOImpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.myBooks.DAO.IFriendDAO;
import com.myBooks.DAO.ILoginDAO;
import com.myBooks.Exception.BookException;
import com.myBooks.constants.CONSTANTS;
import com.myBooks.constants.TABLE;
import com.myBooks.model.VO.SendReqVO;
import com.myBooks.model.dto.ProfileDTO;
import com.myBooks.model.dto.SendReqDTO;

@Repository
@PropertySource("classpath:dbConfig.properties")
public class FriendDAO extends BaseDAO implements IFriendDAO {

	@Autowired
	private Environment envi;

	@Override
	public List<ProfileDTO> listAllUsres() throws BookException {
		List<ProfileDTO> list = new ArrayList<>();
		ProfileDTO profileDTO = null;
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);
			FindIterable<Document> resultObject = collectionProfile.find(); 
			 if (resultObject !=null) { 
				 for (Document document : resultObject) {
					 profileDTO = (new Gson()).fromJson(document.toJson(),ProfileDTO.class); 
					 if(profileDTO != null)
						 list.add(profileDTO);
				}
			 }
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
		return list;

	}

	@Override
	public void sendFriendRequest(SendReqDTO sendReqDTO) throws BookException {
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.FRIEND);
			ObjectMapper mapper = new ObjectMapper();
			Integer generatedrequestId = getNextSequence(dbConnection, "requestId");
			sendReqDTO.setRequestId(String.valueOf(generatedrequestId));
			String json = mapper.writeValueAsString(sendReqDTO);
			collectionProfile.insertOne(Document.parse(json));
		} catch (Exception e) {
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
	}

	@Override
	public void updateFriend(SendReqDTO sendReqDTO) throws BookException {
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.FRIEND);

			Bson filter = new Document("requestId", sendReqDTO.getRequestId());
			Bson newValue = new Document("status", sendReqDTO.getStatus());
			Bson updateOperationDocument = new Document("$set", newValue);
			collectionProfile.updateOne(filter, updateOperationDocument);
			
		} catch (Exception e) {
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
	}

	@Override
	public List<SendReqDTO> listOfFriendRequestPending(SendReqVO sendReqVO) throws BookException {
		List<SendReqDTO> list = null;
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.FRIEND);
			Document document = new Document();
			document.append("friendProfileId", String.valueOf(sendReqVO.getProfileId()));
			document.append("status", CONSTANTS.UNAPPROVED);
			
			FindIterable<Document> resultObject = collectionProfile.find(document);
			MongoCursor<Document> mongoCursor = resultObject.iterator();
			if (mongoCursor != null) {
				list = new ArrayList<>();
				while (mongoCursor.hasNext()) {
					Document friend = mongoCursor.next();
					SendReqDTO responseSendReqDTO = (new Gson()).fromJson(friend.toJson(), SendReqDTO.class);
					if (responseSendReqDTO != null) {
						list.add(responseSendReqDTO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
		return list;
	}

}
