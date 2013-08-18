package com.example.curlingmanagement.rest.client;

public interface IUsersApi {
	
	/**
	 * Sends a request to the server to log in a user.
	 * 
	 * @param username The name of the user.
	 * @param password The user password.
	 * 
	 * @return An authtoken genereted by the server if the login was successful, null otherwise. 
	 */
	public String login(String username, String password);
	
	/**
	 * Sends a request to the server to log out a user.
	 * 
	 * @param username The name of the user
	 * @param authToken An authtoken stored locally on the device, and in the server database.
	 * 
	 * @return true if the logout attempt was successful, false otherwise.
	 */
	public boolean logout(String username, String authToken);
	
	/**
	 * Adds a user to the server database.
	 * 
	 * @param username The name of the user.
	 * @param password The password chosen by the user.
	 * @param email The email address, or null. 
	 * 
	 * @return true if the user was successfully added, false otherwise.
	 */
	public boolean addUser(String username, String password, String email);
	
	/**
	 * Updates a user in the server database.
	 * 
	 * @param username The name of the user. Cannot be changed.
	 * @param password The password of the user. Cannot be null.
	 * @param email The email address. Can be null. 
	 * 
	 * @return true if the user was successfully updated, false otherwise.
	 */
	public boolean updateUser(String username, String password, String email);
	
	/**
	 * Deletes a user in the server database.
	 * 
	 * @param username The name of the user.
	 * @param password The password of the user.
	 * 
	 * @return true if the user was successfully deleted, false otherwise.
	 */
	public boolean deleteUser(String username, String password);	
}
