package edu.memphis.iis.demosurvey;

/**
 * Our very simple model class
 */
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
	public boolean isValid() {
		if (Utils.isBlankString(participantCode)) {
			return false;
		}
		return true;
	}

	public String getParticipantCode() {
		return participantCode;
	}
	public void setParticipantCode(String participantCode) {
		this.participantCode = participantCode;
	}

	public String getFavoriteDogBreed() {
		return favoriteDogBreed;
	}
	public void setFavoriteDogBreed(String favoriteDogBreed) {
		this.favoriteDogBreed = favoriteDogBreed;
	}

	public boolean isCatLover() {
		return catLover;
	}
	public void setCatLover(boolean catLover) {
		this.catLover = catLover;
	}

	public int getFavoriteNumber() {
		return favoriteNumber;
	}
	public void setFavoriteNumber(int favoriteNumber) {
		this.favoriteNumber = favoriteNumber;
	}
}
