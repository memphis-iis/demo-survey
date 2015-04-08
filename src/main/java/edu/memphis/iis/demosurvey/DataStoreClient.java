package edu.memphis.iis.demosurvey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

/**
 * This is a very simple demonstration of an interface to a data store,
 * sometimes called a DAO. Depending on what data you're working with,
 * if you're using something like Spring or Guice, and how large your
 * project is, you might have much more complicated DAO's, you might
 * skip them entirely, or they might be provided by a library.
 */
public class DataStoreClient {
	private final static Logger logger = LoggerFactory.getLogger(DataStoreClient.class);

	protected AmazonDynamoDBClient client;
	protected DynamoDB db;

	public DataStoreClient() {
		// Note our lack of credentials - this is because in Elastic Beanstalk
		// we will be specifying AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
		// via environment variables
		client = new AmazonDynamoDBClient(new DefaultAWSCredentialsProviderChain());

		String endpoint = System.getProperty("aws.dynamoEndpoint");
		if (!Utils.isBlankString(endpoint)) {
			//No env vars for security - we must be in testing
			logger.info("Using specified endpoint " + endpoint);
			client.setEndpoint(endpoint);
		}
		db = new DynamoDB(client);
	}

	/**
	 * Insure that the current database schema is properly loaded.
	 * This example pattern creates a set of tables needed, removes
	 * any previously created tables from that set, and then creates
	 * any tables left over.
	 *
	 * <p>Note that for a larger project, you would probably want something
	 * that could infer table attributes from a model class or vice versa.
	 * Currently we need to make changes to the survey entity here AND in the
	 * the Survey model class
	 */
	public void ensureSchema() {
		Set<String> tablesNeeded = new HashSet<>();
		tablesNeeded.add("Survey");

		for(Table t: db.listTables()) {
			String tableName = t.getDescription().getTableName();
			logger.info("Table Found: " + tableName);
			tablesNeeded.remove(tableName);
		}

		//Create any tables we didn't find
		if (tablesNeeded.contains("Survey")) {
			logger.info("CREATING Table: Survey");

			List<AttributeDefinition> attrDefs = new ArrayList<>();
			attrDefs.add(new AttributeDefinition()
				.withAttributeName("participantCode")
				.withAttributeType("S"));

			List<KeySchemaElement> keySchema = new ArrayList<>();
			keySchema.add(new KeySchemaElement()
				.withAttributeName("participantCode")
				.withKeyType(KeyType.HASH));

			db.createTable(new CreateTableRequest()
				.withTableName("Survey")
				.withKeySchema(keySchema)
				.withAttributeDefinitions(attrDefs)
				.withProvisionedThroughput(new ProvisionedThroughput()
					.withReadCapacityUnits(2L)
					.withWriteCapacityUnits(5L))
			);
		}
	}

	/**
	 * Persist the given survey
	 * @param survey the object to save
	 */
	public void saveSurvey(Survey survey) {
		//TODO
	}
}
