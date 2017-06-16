package com.myBooks.DAOImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.myBooks.DAO.ILoginDAO;
import com.myBooks.Exception.BookException;
import com.myBooks.constants.CONSTANTS;
import com.myBooks.constants.TABLE;
import com.myBooks.model.VO.ChangePasswordRes;
import com.myBooks.model.VO.ProfileVO;
import com.myBooks.model.dto.AddressDTO;
import com.myBooks.model.dto.EmailDTO;
import com.myBooks.model.dto.OtpDTO;
import com.myBooks.model.dto.PhoneDTO;
import com.myBooks.model.dto.ProfileDTO;

@Repository
@PropertySource("classpath:dbConfig.properties")
public class LoginDAO extends BaseDAO implements ILoginDAO {

	@Autowired
	private Environment envi;

	public ProfileDTO signUp(ProfileDTO profileDTO) throws BookException {
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collection = dbConnection.getCollection(TABLE.PROFILE);
			ObjectMapper mapper = new ObjectMapper();
			Integer generatedProfileId = getNextSequence(dbConnection, "profileId");
			profileDTO.setProfileId(generatedProfileId);
			String json = mapper.writeValueAsString(profileDTO);
			collection.insertOne(Document.parse(json));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
		return profileDTO;
	}

	public ProfileDTO login(ProfileVO profileVO) throws BookException {
		MongoClient mongoClient = getDBConnection();
		ProfileDTO profileDTO = null;
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			if ((profileVO.getEmail() != null && profileVO.getPassword() != null) || (profileVO.getPhoneNumber() != null && profileVO.getPassword() != null)) {
				List<BasicDBObject> assetSearch = new ArrayList<BasicDBObject>();
				assetSearch.add(new BasicDBObject("emailList.email", Pattern.compile(profileVO.getEmail(), Pattern.CASE_INSENSITIVE)));
				assetSearch.add(new BasicDBObject("phoneList.phone", Pattern.compile(profileVO.getPhoneNumber(), Pattern.CASE_INSENSITIVE)));
				BasicDBObject andQuery = new BasicDBObject();
				andQuery.put("$or", assetSearch);
				andQuery.put("password", profileVO.getPassword());
				Document resultIterable = collectionProfile.find(andQuery).first();
				
				/*Document whereQuery = new Document();
				if(profileVO.getEmail() != null)
					whereQuery.put("emailList.email", profileVO.getEmail());
				if(profileVO.getPhoneNumber() != null)
					whereQuery.put("phoneList.phone", profileVO.getPhoneNumber());
				whereQuery.put("password", profileVO.getPassword());
				Document resultObject = collectionProfile.find(whereQuery).first();*/
				if (resultIterable != null) {
					profileDTO = (new Gson()).fromJson(resultIterable.toJson(), ProfileDTO.class);
				} else {
					throw new BookException(CONSTANTS.USER_NOT_EXISTS_WITH_THE_GIVEN_EMAIL,
							CONSTANTS.USER_NOT_EXISTS_WITH_THE_GIVEN_EMAIL_CODE);
				}
			}
		} catch (Exception e) {
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}
		return profileDTO;
	}

	public void updateAccessToken(Integer profileId, String accessToken) throws BookException {
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			if (profileId != null && accessToken != null) {
				Bson filter = new Document("profileId", profileId);
				Bson newValue = new Document("accessToken", accessToken);
				Bson updateOperationDocument = new Document("$set", newValue);
				collectionProfile.updateOne(filter, updateOperationDocument);
			}
		} catch (Exception e) {
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
	}

	public Boolean validate(ProfileVO profileVO) throws BookException {
		MongoClient mongoClient = getDBConnection();
		ProfileDTO profileDTO = null;
		Boolean exists = false;
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			BasicDBObject whereQuery = new BasicDBObject();
			if (profileVO.getEmail() != null) {
				whereQuery.append("emailList.email", profileVO.getEmail());
			}else if(profileVO.getPhoneNumber() != null){
				whereQuery.append("phoneList.phoneNumber", profileVO.getPhoneNumber());
			}
			Document resultObject = collectionProfile.find(whereQuery).first(); 
			 if (resultObject !=null) { 
				 profileDTO = (new Gson()).fromJson(resultObject.toJson(),ProfileDTO.class); 
			 }
			if (profileDTO != null && profileDTO.getProfileId() != null) {
				exists = true;
			} else {
				exists = false;
			}
			mongoClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			mongoClient.close();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}
		return exists;
	}

	@Override
	public ProfileDTO validateLogin(ProfileVO profileVO) throws BookException {
		MongoClient mongoClient = getDBConnection();
		ProfileDTO profileDTO = null;
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			BasicDBObject whereQuery = new BasicDBObject();
			if (profileVO.getEmail() != null) {
				whereQuery.append("emailList.email", profileVO.getEmail());
				whereQuery.append("password", profileVO.getPassword());
			}else if(profileVO.getPhoneNumber() != null){
				whereQuery.append("phoneList.phoneNumber", profileVO.getPhoneNumber());
				whereQuery.append("password", profileVO.getPassword());
			}
			Document resultObject = collectionProfile.find(whereQuery).first(); 
			 if (resultObject !=null) { 
				 profileDTO = (new Gson()).fromJson(resultObject.toJson(),ProfileDTO.class); 
			 }
		} catch (Exception e) {
			e.printStackTrace();
			mongoClient.close();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}finally{
			mongoClient.close();
		}
		return profileDTO;
	}

	@Override
	public ProfileDTO validateAccessToken(String profileId,String accessToken) throws BookException {
		MongoClient mongoClient = getDBConnection();
		ProfileDTO profile = null;
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			BasicDBObject whereQuery = new BasicDBObject();
			if (profileId != null && accessToken != null) {
				whereQuery.append("profileId", Integer.valueOf(profileId));
				whereQuery.append("accessToken", accessToken);
			}
			Document resultObject = collectionProfile.find(whereQuery).first();
			if(resultObject != null){
				profile = (new Gson()).fromJson(resultObject.toJson(),ProfileDTO.class); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}finally{
			mongoClient.close();
		}
		return profile;
	}

	@Override
	public Boolean changePassword(ChangePasswordRes changePasswordRes) throws BookException{
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);
			
			BasicDBObject whereQuery = new BasicDBObject();
			if (changePasswordRes.getProfileId() != null && changePasswordRes.getOldPassword() != null) {
				whereQuery.append("profileId", changePasswordRes.getProfileId());
				whereQuery.append("password", changePasswordRes.getOldPassword());
			}
			Document resultObject = collectionProfile.find(whereQuery).first(); 
			if (resultObject !=null) { 
				if (changePasswordRes.getOldPassword() != null && changePasswordRes.getNewPassword() != null) {
					Bson filter = new Document("profileId", changePasswordRes.getProfileId());
					Bson newValue = new Document("password", changePasswordRes.getNewPassword());
					Bson updateOperationDocument = new Document("$set", newValue);
					collectionProfile.updateOne(filter, updateOperationDocument);
					return true;
				}
			}else{
				return false;
			}
			
		} catch (Exception e) {
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
		return true;
	}

	@Override
	public ProfileDTO getProfileByEmailorPhone(ProfileVO profileVO) throws BookException {
		MongoClient mongoClient = getDBConnection();
		ProfileDTO profileDTO = null;
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			BasicDBObject whereQuery = new BasicDBObject();
			if (profileVO.getEmail() != null) {
				whereQuery.append("emailList.email", profileVO.getEmail());
			}
			if (profileVO.getPhoneNumber() != null) {
				whereQuery.append("phoneList.phoneNumber", profileVO.getPhoneNumber());
			}
			Document resultObject = collectionProfile.find(whereQuery).first(); 
			if (resultObject !=null) {
				profileDTO = (new Gson()).fromJson(resultObject.toJson(),ProfileDTO.class); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}finally{
			mongoClient.close();
		}
		return profileDTO;
	}

	@Override
	public void updateOtp(OtpDTO otp) throws BookException {
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collection = dbConnection.getCollection(TABLE.OTP);
			ObjectMapper mapper = new ObjectMapper();
			Integer otpId = getNextSequence(dbConnection, "otpId");
			otp.setOtpId(otpId);
			String json = mapper.writeValueAsString(otp);
			collection.insertOne(Document.parse(json));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
	}

	@Override
	public OtpDTO validateForgotPassword(ProfileDTO profile) throws BookException {
		MongoClient mongoClient = getDBConnection();
		OtpDTO otpDTO = null;
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.OTP);

			BasicDBObject whereQuery = new BasicDBObject();
			if (profile.getProfileId() != null) {
				whereQuery.append("profileId", profile.getProfileId());
			}
			Document resultObject = collectionProfile.find(whereQuery).sort(new BasicDBObject().append("otpId", -1)).first();
			if (resultObject !=null) { 
				otpDTO = (new Gson()).fromJson(resultObject.toJson(),OtpDTO.class); 
			}
			
		} catch (Exception e) {
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}finally{
			mongoClient.close();
		}
		return otpDTO;
	}

	@Override
	public void updatePassword(Integer profileId, String password) throws BookException {
		MongoClient mongoClient = getDBConnection();
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			if (profileId != null) {
				Bson filter = new Document("profileId", profileId);
				Bson newValue = new Document("password", password);
				Bson updateOperationDocument = new Document("$set", newValue);
				collectionProfile.updateOne(filter, updateOperationDocument);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		} finally {
			mongoClient.close();
		}
	}

	@Override
	public ProfileDTO getProfileById(String loggedInProfileId) throws BookException {
		MongoClient mongoClient = getDBConnection();
		ProfileDTO profileDTO = null;
		try {
			MongoDatabase dbConnection = mongoClient.getDatabase(envi.getProperty("db.name"));
			MongoCollection<Document> collectionProfile = dbConnection.getCollection(TABLE.PROFILE);

			BasicDBObject whereQuery = new BasicDBObject();
			if (loggedInProfileId != null) {
				whereQuery.append("profileId", Integer.parseInt(loggedInProfileId));
			}
			Document resultObject = collectionProfile.find(whereQuery).first(); 
			if (resultObject !=null) {
				profileDTO = (new Gson()).fromJson(resultObject.toJson(),ProfileDTO.class); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BookException(CONSTANTS.UNEXPECTED_ERROR_MESSAGE, CONSTANTS.UNEXPECTED_ERROR_CODE);
		}finally{
			mongoClient.close();
		}
		return profileDTO;
	}
}
