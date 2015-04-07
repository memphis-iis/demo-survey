package edu.memphis.iis.demosurvey;

public class Survey {
	protected String participantCode;
	protected String favoriteDogBreed;
	protected boolean catLover;
	protected int favoriteNumber;
	
	public Survey() {
		//No set up action
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
