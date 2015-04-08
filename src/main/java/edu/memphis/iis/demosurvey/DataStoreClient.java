package edu.memphis.iis.demosurvey;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/**
 * This is a very simple demonstration of an interface to a data store,
 * sometimes called a DAO. Depending on what data you're working with,
 * if you're using something like Spring or Guice, and how large your
 * project is, you might have much more complicated DAO's, you might
 * skip them entirely, or they might be provided by a library
 */
public class DataStoreClient {
	protected AmazonDynamoDBClient client;
	protected DynamoDB db;

	public DataStoreClient() {
		// Note our lack of credentials - this is because in Elastic Beanstalk
		// we will be specifying AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
		// via environment variables
		client = new AmazonDynamoDBClient();
		db = new DynamoDB(client);
	}

	/**
	 * Insure that the current database schema is properly loaded
	 */
	public void ensureSchema() {
		//TODO
	}

	/**
	 * Persist the given survey
	 * @param survey the object to save
	 */
	public void saveSurvey(Survey survey) {
		//TODO
	}
}
