package edu.memphis.iis.demosurvey;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;


/**
 * This is a very simple demonstration of an interface to a data store,
 * sometimes called a DAO. Depending on what data you're working with,
 * if you're using something like Spring or Guice, and how large your
 * project is, you might have much more complicated DAO's, you might
 * skip them entirely, or they might be provided by a library.
 *
 * Note that we manually create our DynamoDB client, db, and mapper
 * instances in the constructor. In a large system, we would want to
 * use an Inversion of Control (aka Dependency Injection) pattern.
 * Those instances would be injected by some kind of context object that
 * could vary for unit tests, local workstation debugging, and running
 * in the actual AWS cloud
 *
 * For this simple demo, we are just using some custom logic based on
 * system properties. See the pom.xml for how we set aws.dynamoEndpoint
 * for local Tomcat testing AND the AWS credential properties so that
 * our use of DefaultAWSCredentialsProviderChain works
 */
public class DataStoreClient {
	private final static Logger logger = LoggerFactory.getLogger(DataStoreClient.class);

	/** Created and managed by constructor */
	private AmazonDynamoDBClient client;

	/** Created and managed by helper function - shouldn't be used directly in code */
	private DynamoDB db;

	/** Created and managed by helper function - shouldn't be used directly in code */
	private DynamoDBMapper mapper;

	/** Default constructor */
	public DataStoreClient() {
		// Note our lack of credentials - this is because in Elastic Beanstalk
		// we will be specifying AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
		// via environment variables
		client = new AmazonDynamoDBClient(new DefaultAWSCredentialsProviderChain());

		String endpoint = System.getProperty("aws.dynamoEndpoint");
		if (!Utils.isBlankString(endpoint)) {
			//No env vars for security - we must be in testing
			logger.info("Using specified endpoint {}", endpoint);
			client.setEndpoint(endpoint);
		}

		//These will be lazy-init'ed by our helper functions
		db = null;
		mapper = null;
	}

	/**
	 * Simple helper to lazy-create the (low-level) Dynamo DB instance for
	 * our current client.
	 * @return valid instance of Dynamo DB
	 */
	protected DynamoDB getDB() {
		if (db == null) {
			db = new DynamoDB(client);
		}
		return db;
	}

	/**
	 * Simple helper to lazy-create the (high-level) Dynamo DB mapper instance
	 * for our current client.
	 * @return valid instance of Dynamo DB Mapper
	 */
	protected DynamoDBMapper getMapper() {
		if (mapper == null) {
			mapper = new DynamoDBMapper(client);
		}
		return mapper;
	}

	/**
	 * Insure that the current database schema is properly loaded.
	 * This example pattern creates a set of tables needed, removes
	 * any previously created tables from that set, and then creates
	 * any tables left over.
	 */
	public void ensureSchema() {
		Set<String> tablesNeeded = new HashSet<>();
		tablesNeeded.add("Survey");

		for(Table t: getDB().listTables()) {
			String tableName = t.getTableName();
			logger.info("Table Found: " + tableName);
			tablesNeeded.remove(tableName);
		}

		//Create any tables we didn't find
		if (tablesNeeded.contains("Survey")) {
			logger.info("CREATING Table: Survey");

			db.createTable(getMapper()
				.generateCreateTableRequest(Survey.class)
				.withProvisionedThroughput(new ProvisionedThroughput()
					.withReadCapacityUnits(2L)
					.withWriteCapacityUnits(5L)
				)
			);
		}
	}

	/**
	 * Persist the given survey
	 * @param survey the object to save
	 */
	public void saveSurvey(Survey survey) {
		if (survey == null || !survey.isValid()) {
			throw new IllegalArgumentException("Invalid survey cannot be saved");
		}
		getMapper().save(survey);
	}
}
