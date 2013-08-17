package com.example.curlingmanagement.userinterface;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A game of curling. Contains information needed by the menu. Implementation
 * of the detailed state of a game will be located in the game module.
 * 
 * THIS CLASS WILL BE CHANGED LATER ON
 * 
 * @author Fabian
 *
 */
public class Game implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;
	
	public User mOpponent;
	public int mUserScore;
	public int mOpponentScore;
	public int mStonesPlayed;
	public boolean mIsHome;
	public String mId;
	
	/**
	 * Constructor.
	 * 
	 * @param opponent
	 * @param isHome
	 */
	public Game(User opponent, boolean isHome) {
		mOpponent = opponent;
		mUserScore = 0;
		mOpponentScore = 0;
		mStonesPlayed = 0;
		mIsHome = isHome;
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
		this.setId(timestamp.toString());
	}

	/**
	 * Used to compare an object with a game. Match of id is enough to consider a game
	 * equal to this game.
	 * 
	 * @return boolean true if a match is found.
	 */
	public boolean equals(Object object) {
		if(object == null) return false;
		if(!(object instanceof Game)) return false;
		Game game = (Game) object;
		return game.getId().equals(this.getId());		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getStonesPlayed() {
		return mStonesPlayed;
	}

	public void setStonesPlayed(int stonesPlayed) {
		this.mStonesPlayed = stonesPlayed;
	}

	public User getOpponent() {
		return mOpponent;
	}

	public void setOpponent(User opponent) {
		mOpponent = opponent;
	}

	public int getUserScore() {
		return mUserScore;
	}

	public void setUserScore(int userScore) {
		mUserScore = userScore;
	}

	public int getOpponentScore() {
		return mOpponentScore;
	}

	public void setOpponentScore(int opponentScore) {
		mOpponentScore = opponentScore;
	}
	
	public boolean isHomeGame() {
		return mIsHome;
	}

	public String getId() {
		return mId;
	}

	private void setId(String id) {
		this.mId = id;
	}

	public static final Parcelable.Creator<Game> CREATOR =
			new Parcelable.Creator<Game>(){
		@Override
		public Game createFromParcel(Parcel source) {
			return new Game(source);
		}

		@Override
		public Game[] newArray(int size) {
			return new Game[size];
		}
	};
	
	public Game(Parcel source){
		  readFromParcel(source);
		 }
	
	 @Override
	 public int describeContents() {
	  return 0;
	 }

	 @Override
	 public void writeToParcel(Parcel dest, int flags) {
	  dest.writeSerializable(mOpponent);
	  dest.writeInt(mUserScore);
	  dest.writeInt(mOpponentScore);
	  dest.writeInt(mStonesPlayed);
	  dest.writeByte((byte) (mIsHome ? 1 : 0));
	  dest.writeString(mId);
	 }
			 
	public void readFromParcel(Parcel source){
		  mOpponent = (User) source.readSerializable();
		  mUserScore = source.readInt();
		  mOpponentScore = source.readInt();
		  mStonesPlayed = source.readInt();
		  mIsHome = source.readByte() != 0;
		  mId = source.readString();
	}	
}
