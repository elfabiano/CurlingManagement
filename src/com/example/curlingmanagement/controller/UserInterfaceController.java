package com.example.curlingmanagement.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.example.curlingmanagement.Constants;
import com.example.curlingmanagement.resources.model.Game;

/**
 * The controller of the user interface module, implemented as a singelton class. 
 * Keeps a static instance of itself in order to be reachable from anywhere
 * in the program.
 * 
 * @author Fabian
 *
 */
public class UserInterfaceController implements IUserInterface {

	private static UserInterfaceController mInstance;
	
	/**
	 * Default constructor
	 */
	private UserInterfaceController() {
	}

	/**
	 * Initializes the class
	 */	
	public static void initialize() {
		if(mInstance == null) {
			mInstance = new UserInterfaceController();
		}
	}
	
	/**
	 * Used to get an instance of this class.
	 * 
	 * @return instance of the class
	 */
	public static UserInterfaceController getInstance() {
		return mInstance;
	}

	/**
	 * Initializes a new session.
	 * 
	 * @param context
	 */
	public void initSession(String username, String authToken, Context context) {
		
		//This is to simplify the implementation. More thorough authentication will be needed later on.
		//mCurrentSession.changeUsername("username");
	}

	/**
	 * Communicates with server to search for an opponent
	 * 
	 * @return new game if opponent was found within a certain time,
	 *  null otherwise
	 */
	public Game searchRandomOpponent() {		
		return null;
	}
	
	/**
	 * Starts a new game.
	 * @param opponentName
	 */
	public void newGame(String opponentName) {
	}

	/**
	 * Removes the game from the list of ongoing games
	 * 
	 * @param game
	 */
	public void resignGame(Game game) {			
	}

	/**
	 * Saves ongoing games to internal storage.
	 * @param context
	 */
	/*public void saveOngoingGames(Context context) {
		ArrayList<Game> ongoingGames = mCurrentSession.getUser().getOngoingGames();
		String username = mCurrentSession.getUser().getUsername();

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = context.openFileOutput(username + Constants.GAMES_FILENAME, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeInt(ongoingGames.size());
			for(Game game:ongoingGames) {
				oos.writeObject(game);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {			
			try {
				if (fos != null)
					fos.close();
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}*/
	
	/**
	 * Loads ongoing games from internal storage.
	 * @param context
	 */
	/*public void loadOngoingGames(Context context) {
		String username = mCurrentSession.getUser().getUsername();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(username + Constants.GAMES_FILENAME);
			ois = new ObjectInputStream(fis);
			int cnt = ois.readInt();
			for(int i = 0; i < cnt; i++) {
				mCurrentSession.getUser().getOngoingGames().add((Game) ois.readObject());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				if (fis != null)
					fis.close();
				if (ois != null)
					ois.close();			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
}
