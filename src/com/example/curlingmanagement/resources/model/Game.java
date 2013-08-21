package com.example.curlingmanagement.resources.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class Game {
	private int mId;
	private String mStatus;
	private int mCurrentStateId;
	private int mPreviousStateId;
	private int mHomeScore;
	private int mAwayScore;
	private int mStonesPlayed;
	private String mHomeUsername;
	private String mAwayUsername;
	private String mModified;
	
	public Game() {
		
	}
	
	public Game(String username) {
		mStatus = "pending";
		mHomeScore = 0;
		mAwayScore = 0;
		mStonesPlayed = 0;

		Random rand = new Random();
		if(rand.nextInt(2) == 0) {
			mHomeUsername = username;
		} else {
			mAwayUsername = username;
		}
		
		Date date = new Date();
		mModified = new Timestamp(date.getTime()).toString();
	}
	
	public Game(int id, String status, int currentStateId, int previousStateId, int homeScore, 
			int awayScore, int stonesPlayed, String homeUsername, String awayUsername, String modified) {
		mId = id;
		mStatus = status;
		mCurrentStateId = currentStateId;
		mPreviousStateId = previousStateId;
		mHomeScore = homeScore;
		mAwayScore = awayScore;
		mStonesPlayed = stonesPlayed;
		mHomeUsername = homeUsername;
		mAwayUsername = awayUsername;
		mModified = modified;
	}
	
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		this.mId = id;
	}
	public String getStatus() {
		return mStatus;
	}
	public void setStatus(String status) {
		this.mStatus = status;
	}
	public int getCurrentStateId() {
		return mCurrentStateId;
	}
	public void setCurrentStateId(int currentStateId) {
		this.mCurrentStateId = currentStateId;
	}
	public int getPreviousStateId() {
		return mPreviousStateId;
	}
	public void setPreviousStateId(int previousStateId) {
		this.mPreviousStateId = previousStateId;
	}
	public int getHomeScore() {
		return mHomeScore;
	}
	public void setHomeScore(int homeScore) {
		this.mHomeScore = homeScore;
	}
	public int getAwayScore() {
		return mAwayScore;
	}
	public void setAwayScore(int awayScore) {
		this.mAwayScore = awayScore;
	}
	public int getStonesPlayed() {
		return mStonesPlayed;
	}
	public void setStonesPlayed(int stonesPlayed) {
		this.mStonesPlayed = stonesPlayed;
	}
	public String getHomeUsername() {
		return mHomeUsername;
	}
	public void setHomeUsername(String homeUsername) {
		this.mHomeUsername = homeUsername;
	}
	public String getAwayUsername() {
		return mAwayUsername;
	}
	public void setAwayUsername(String awayUsername) {
		this.mAwayUsername = awayUsername;
	}
	public String getModified() {
		return mModified;
	}
	public void setModified(String modified) {
		this.mModified = modified;
	}
}
