package com.myBooks.DAOImpl;

import java.util.Arrays;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


@PropertySource("classpath:dbConfig.properties")
public class BaseDAO {

	@Autowired
	private Environment envi;
	
	@SuppressWarnings("resource")
	public MongoClient getDBConnection(){
		//MongoCredential credential = MongoCredential.createCredential(envi.getProperty("db.username"), envi.getProperty("db.name"), envi.getProperty("db.password").toCharArray());
	    ServerAddress serverAddress = new ServerAddress(envi.getProperty("db.ip"), Integer.valueOf(envi.getProperty("db.port")));

	    // Mongo Client
	   // MongoClient mongoClient = new MongoClient(serverAddress,Arrays.asList(credential)); 
	    MongoClient mongoClient = new MongoClient(serverAddress);
		//MongoClient mongo = new MongoClient(envi.getProperty("db.ip"), Integer.valueOf(envi.getProperty("db.port")));
		return mongoClient;
	}
	
	public Integer getNextSequence(MongoDatabase dbConnection, String idName){
		MongoCollection<Document> collection = dbConnection.getCollection("Counter");
		Document counter = new Document();
		counter.put("idName", idName);
		FindIterable<Document> iterable = collection.find(counter);
		MongoCursor<Document> cursor = iterable.iterator();
        if (cursor.hasNext()){
        	Document counterDBObject  =  cursor.next();
        	Integer sequence = (Integer) counterDBObject.get("sequence");
        	sequence++;
        	System.out.println("vicky : "+ sequence);
        	counterDBObject.put("sequence", sequence);
        	collection.findOneAndReplace(counter, counterDBObject);
            return sequence;
        }else{
        	counter.put("sequence", 1);
        	collection.insertOne(counter);
        	return 1;
        }
	}
}
