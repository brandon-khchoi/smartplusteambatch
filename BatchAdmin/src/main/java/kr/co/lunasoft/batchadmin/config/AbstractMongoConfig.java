package kr.co.lunasoft.batchadmin.config;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import lombok.Data;

@Data
public abstract class AbstractMongoConfig {

	//Mongo DB Properties
	private String uri, database;
	
	/*
	 * Method that creates MongoDbFactory Common to both of the MongoDb connections
	 */
	public MongoDbFactory mongoDbFactory() {
		return new SimpleMongoDbFactory(getMongoClient(), database);
	}

	/*
	 * Method that creates MongoClient
	 */
	private MongoClient getMongoClient() {
		return new MongoClient(new MongoClientURI(uri));
	}
	
	/*
	 * Factory method to create the MongoTemplate
	 */
	abstract public MongoTemplate getMongoTemplate();
}
