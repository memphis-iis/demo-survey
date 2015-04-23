package edu.memphis.iis.demosurvey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
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
 * our use of DefaultAWSCredentialsProviderChain works.
 *
 * Note that this class supports auto-creation of all tables listed in
 * the TABLES variable. It also supports automatic key checking (which
 * we use in saveSurvey).
 */
public class DataStoreClient {
	private final static Logger logger = LoggerFactory.getLogger(DataStoreClient.class);

	/**
	 * The list of tables that should be insured (created if missing) on
	 * startup. Note that if there is a class in this array that isn't
	 * annotated with @DynamoDBTable with tableName specified you WILL
	 * get exceptions on startup).
	 * */
    private final static Class<?>[] TABLES = {Survey.class};

    /** Default read capacity set for DynamoDB tables on creation */
    private final static long DEFAULT_READ_CAPACITY = 2L;

    /** Default write capacity set for DynamoDB tables on creation */
    private final static long DEFAULT_WRITE_CAPACITY = 5L;

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
		Map<String, Class<?>> tablesNeeded = new HashMap<>();

		//Extract table names from table classes
		for(Class<?> c: TABLES) {
		    DynamoDBTable ann = (DynamoDBTable)c.getAnnotation(DynamoDBTable.class);
		    tablesNeeded.put(ann.tableName(), c);
		    logger.info("Table Configuration Found: " + ann.tableName());
		}

		for(Table t: getDB().listTables()) {
			String tableName = t.getTableName();
			logger.info("Existing Table Found: " + tableName);
			tablesNeeded.remove(tableName);
		}

		//Create any tables we didn't find
		for(String tableName: tablesNeeded.keySet()) {
		    logger.info("CREATING Table: " + tableName);

		    Class<?> tableClass = tablesNeeded.get(tableName);

		    db.createTable(getMapper()
	            .generateCreateTableRequest(tableClass)
                .withProvisionedThroughput(new ProvisionedThroughput()
                    .withReadCapacityUnits(DEFAULT_READ_CAPACITY)
                    .withWriteCapacityUnits(DEFAULT_WRITE_CAPACITY)
                )
            );
		}
	}

	/**
	 * Persist the given survey
	 *
	 * @param survey the object to save
	 * @param allowOverwrite if true, a previous record will overwritten
	 */
	public void saveSurvey(Survey survey, boolean allowOverwrite) {
		if (survey == null || !survey.isValid()) {
			throw new IllegalArgumentException("Invalid survey cannot be saved");
		}

		if (allowOverwrite) {
		    //Just fire a save and completely overwrite the original record
		    getMapper().save(
		        survey,
		        new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
		    );
		}
		else {
		    //Throw an exception if the record already exists
		    getMapper().save(
	            survey,
	            new DynamoDBSaveExpression()
	                .withExpected(expectKey(Survey.class))
	        );
		}
	}

	/**
	 * Return a list of all surveys. Note that the underlying implementation
	 * of the list is unspecified. Currently we use the AWS SDK's lazy-loading
	 * list (which returns a page at a time). There may be delays while iterating
	 * over this list, AND this may change in the future
	 *
	 * @return a List<> of Survey instances, or a List<> of size 0 if no
	 *         Survey's are found
	 */
	public List<Survey> findSurveys() {
	    return getMapper().scan(
            Survey.class,
            new DynamoDBScanExpression()
        );
	}

	/**
	 * Given a "table" (the Class<?> for a class that is annotated with
	 * DynamoDBTable), return the appropriate map. NOTE that if you don't
	 * have a method annotated with DynamoDBHashKey and attributeName
	 * specified, an exception will be thrown
	 * @param table instance of Class<?>
	 * @return a Map suitable for use as an Expected in a DynamoDBSaveExpression
	 */
	private Map<String, ExpectedAttributeValue> expectKey(Class<?> table) {
	    String keyName = keyAttributeName(table);
	    if (Utils.isBlankString(keyName)) {
	        throw new IllegalArgumentException(table.getCanonicalName() + " has no DynamoDBHashKey specified");
	    }

	    Map<String, ExpectedAttributeValue> expected = new HashMap<>();
        expected.put(keyName, new ExpectedAttributeValue(false));
        return expected;
	}

	/**
	 * Given a table (the Class<?> for a class that is annotated with
     * DynamoDBTable), return the attribute name of the key to the table
     * as specified by the DynamoDBHashKey annotation
	 * @param table instance of Class<?> for a class annotated DynamoDBTable
	 * @return the attribute name of the key for the table
	 */
	private String keyAttributeName(Class<?> table) {
	    for(Method m: table.getMethods()) {
	         Annotation keyAttr = m.getAnnotation(DynamoDBHashKey.class);
	        if (keyAttr != null) {
	            return ((DynamoDBHashKey)keyAttr).attributeName();
	        }
	    }

	    return null; //Not found
	}
}
