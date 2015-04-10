package edu.memphis.iis.demosurvey;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Our very simple model class
 */
@DynamoDBTable(tableName="Survey")
public class Survey {
	protected String participantCode;
	protected String favoriteDogBreed;
	protected boolean catLover;
	protected int favoriteNumber;

	public Survey() {
		//No set up action
	}

	/**
	 * Insure that the current instance is valid for writing to the
	 * data store
	 * @return true if valid
	 */
	@DynamoDBIgnore // We don't want this stored in the DB
	public boolean isValid() {
		if (Utils.isBlankString(participantCode)) {
			return false;
		}
		return true;
	}

	@DynamoDBHashKey(attributeName="participantCode")
	public String getParticipantCode() {
		return participantCode;
	}
	public void setParticipantCode(String participantCode) {
		this.participantCode = participantCode;
	}

	@DynamoDBAttribute(attributeName="favoriteDogBreed")
	public String getFavoriteDogBreed() {
		return favoriteDogBreed;
	}
	public void setFavoriteDogBreed(String favoriteDogBreed) {
		this.favoriteDogBreed = favoriteDogBreed;
	}

	@DynamoDBAttribute(attributeName="catLover")
	public boolean isCatLover() {
		return catLover;
	}
	public void setCatLover(boolean catLover) {
		this.catLover = catLover;
	}

	@DynamoDBAttribute(attributeName="favoriteNumber")
	public int getFavoriteNumber() {
		return favoriteNumber;
	}
	public void setFavoriteNumber(int favoriteNumber) {
		this.favoriteNumber = favoriteNumber;
	}
}
