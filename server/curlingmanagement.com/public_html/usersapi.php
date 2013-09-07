<?php
    
	/* 
		This is an example class script proceeding secured API
		To use this class you should keep same as query string and function name
		Ex: If the query string value rquest=delete_user Access modifiers doesn't matter but function should be
		     function delete_user(){
				 You code goes here
			 }
		Class will execute the function dynamically;
		
		usage :
		
		    $object->response(output_data, status_code);
			$object->_request	- to get santinized input 	
			
			output_data : JSON (I am using)
			status_code : Send status message for headers
			
		Add This extension for localhost checking :
			Chrome Extension : Advanced REST client Application
			URL : https://chrome.google.com/webstore/detail/hgmloofddffdnphfgcellkdfbfbjeloo
		
		I used the below table for demo purpose.
		
		CREATE TABLE IF NOT EXISTS `users` (
		  `user_id` int(11) NOT NULL AUTO_INCREMENT,
		  `user_fullname` varchar(25) NOT NULL,
		  `user_email` varchar(50) NOT NULL,
		  `user_password` varchar(50) NOT NULL,
		  `user_status` tinyint(1) NOT NULL DEFAULT '0',
		  PRIMARY KEY (`user_id`)
		) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
 	*/
	
	require_once("Rest.inc.php");
	
	class API extends REST {
	
		public $data = "";
		
		const DB_SERVER = "curlingmanagement-183795.mysql.binero.se";
		const DB_USER = "183795_ns92352";
		const DB_PASSWORD = "spiskummin";
		const DB = "183795-curlingmanagement";
		
		private $db = NULL;
	
		public function __construct(){
			parent::__construct();				// Init parent contructor
			$this->dbConnect();					// Initiate Database connection
		}
		
		/*
		 *  Database connection 
		*/
		private function dbConnect(){
			$this->db = mysqli_connect(self::DB_SERVER,self::DB_USER,self::DB_PASSWORD);
			if($this->db)
				mysqli_select_db($this->db, self::DB);
		}
		
		/*
		 * Public method for access api.
		 * This method dynamically call the method based on the query string
		 *
		 */
		public function processApi(){
			$func = strtolower(trim(str_replace("/","",$_REQUEST['request'])));
			if((int)method_exists($this,$func) > 0)
				$this->$func();
			else
				$this->response('',404);				// If the method not exist with in this class, response would be "Page not found".
		}
		
		/* 
		 *	Simple login API
		 *  Login must be POST method
		 *  email : <USER EMAIL>
		 *  pwd : <USER PASSWORD>
		 */		
		private function login(){
			// Cross validation if the request method is POST else it will return "Not Acceptable" status
			if($this->get_request_method() != "POST"){
				$this->response('',406);
			}
			
			$username = $this->_request['username'];		
			$password = $this->_request['password'];
			
			// Input validations
			if(!empty($username) and !empty($password)) {					
					do {
					$auth_tkn = $this->new_auth_token();
					$stmt = mysqli_prepare($this->db, 
											"UPDATE users set auth_token = ?, logged_in = 1
											WHERE username = ? AND password = ?");
					mysqli_stmt_bind_param($stmt, "sss", $auth_tkn, $username, $password);
					$success = mysqli_stmt_execute($stmt);
					//$sql = mysql_query("UPDATE users SET auth_token = '$auth_tkn', logged_in = 1 
						//		WHERE username = '$username' AND password = '$password'", $this->db); 
					} while(!$success);
					
					if(mysqli_affected_rows($this->db) > 0) {							
						$auth_tkn_array = array("auth_token" => $auth_tkn);
						// If success everything is good send header as "OK" and user details
						$this->response($this->json($auth_tkn_array), 200);
					} else {
						$this->response('', 204);	// If no records "No Content" status
					}
				}			
			// If invalid inputs "Bad Request" status message and reason
			$error = array('status' => "Failed", "msg" => "Invalid Username or Password");
			$this->response($this->json($error), 400);
		}
		
		private function logout(){
			// Cross validation if the request method is POST else it will return "Not Acceptable" status
			if($this->get_request_method() != "POST"){
				$this->response('',406);
			}
			
			$username = $this->_request['username'];
			$auth_token = $this->_request['auth_token'];		
			
			// Input validations
			if(!empty($username)) {
				$stmt = mysqli_prepare($this->db, 
										"UPDATE users SET logged_in = 0 
										WHERE username = ? AND auth_token = ?");
				mysqli_stmt_bind_param($stmt, "ss", $username, $auth_token);
				$success = mysqli_stmt_execute($stmt);
				
				//$sql = mysql_query("UPDATE users SET logged_in = 0 WHERE username = '$username' 
					//				AND auth_token = '$auth_token'", $this->db);
				if(mysqli_affected_rows($this->db) > 0){
					// If success everything is good send header as "OK" and user details
					$this->response('', 200);
				}else{
					$this->response('', 204);	// If no records "No Content" status
				}
			}
			
			// If invalid inputs "Bad Request" status message and reason
			$error = array('status' => "Failed", "msg" => "Invalid Username or Password");
			$this->response($this->json($error), 400);
		}
		
		/* 
		 * Can be used for signing up
		 */
		private function addUser() {
			if($this->get_request_method() != "POST"){
				$this->response('',406);
			}
			
			$username = $this->_request['username'];
			$password = $this->_request['password'];
			$email = $this->_request['email'];
			
			if(isset($email) && !filter_var($email, FILTER_VALIDATE_EMAIL)) {
				$error = array('status' => "Forbidden", "msg" => "Not a valid email address");
				$this->response($this->json($error), 403);
			}
			// Input validations
			if(isset($username) and isset($password)) {
				$stmt = mysqli_prepare($this->db,
										"INSERT INTO users (username, password, email)
										VALUES(?, ?, ?)");
				mysqli_stmt_bind_param($stmt, "sss", $username, $password, $email);
				$success = mysqli_stmt_execute($stmt);
				
				//$sql = mysql_query("INSERT INTO users (username, password, email) 
									//VALUES ('$username', '$password', '$email')", $this->db);
				/*$sql = mysql_query("INSERT INTO users (username, password, email) VALUES ('$username', '$password', '$email')
									IF NOT(SELECT * FROM users WHERE username == '$username')", $this->db);*/
				if($success) {
					$this->response('', 200);
				} else if(!$success) {
					$error = array('status' => "Failed", 'message' => "User already exists");
					$this->response($this->json($error), 409);
				} 
			}
			// If invalid inputs "Bad Request" status message and reason
			$error = array('status' => "Failed", "msg" => "Invalid Username or Password");
			$this->response($this->json($error), 400);	
		}

		private function updateUser() {
			if($this->get_request_method() != "POST"){
				$this->response('',406);
			}
			
			$username = $this->_request['username'];
			$password = $this->_request['password'];
			$email = $this->_request['email'];
			
			if(isset($email) && !filter_var($email, FILTER_VALIDATE_EMAIL)) {
				$error = array('status' => "Forbidden", "msg" => "Not a valid email address");
				$this->response($this->json($error), 403);
			}
				
			// Input validations
			if(isset($username) and isset($password)) {
				$stmt = mysqli_prepare($this->db, 
										"UPDATE users SET password = ?, email = ?
										WHERE username = ?");
				mysqli_stmt_bind_param($stmt, "sss", $password, $email, $username);
				$success = mysqli_stmt_execute($stmt);
				
				//$sql = mysql_query("UPDATE users SET password = '$password', email = '$email'
				//		WHERE username = '$username'", $this->db);
				
				if(mysqli_affected_rows($this->db) > 0){
					// If success everything is good send header as "OK" and user details
					$this->response('', 200);
				} else {
					$this->response('', 204);	// If no records "No Content" status
				}
			} else {
				// If invalid inputs "Bad Request" status message and reason
				$error = array('status' => "Failed", "msg" => "Invalid Username or Password");
				$this->response($this->json($error), 400);
			}
		}
		
		private function deleteUser(){
			// Cross validation if the request method is DELETE else it will return "Not Acceptable" status
			if($this->get_request_method() != "POST"){
				$this->response('',406);
			}
			
			$username = $this->_request['username'];		
			$password = $this->_request['password'];
			
			if(isset($username) and isset($password)) {
				$stmt = mysqli_prepare($this->db, 
										"DELETE FROM users WHERE username = ? AND password = ?");
				mysqli_stmt_bind_param($stmt, "ss", $username, $password);
				mysqli_stmt_execute($stmt);
				
				//mysql_query("DELETE FROM users WHERE username = '$username' AND password = '$password'", $this->db);
				if(mysqli_affected_rows($this->db) > 0) {
					$success = array('status' => "Success", "msg" => "Successfully one record deleted.");
					$this->response($this->json($success),200);
				}else{
					$this->response('', 204);	// If no records "No Content" status
				}
			}else{
				// If invalid inputs "Bad Request" status message and reason
				$error = array('status' => "Failed", "msg" => "Invalid Username or Password");
				$this->response($this->json($error), 400);	
			}
		}
		
		private function new_auth_token() {
			$chars = "abcdefghijklmnopqrstuvwxyz";
			$size = strlen($chars);
			$str = "";
	
			for($i = 0; $i < 8; $i++) {
				$str .= $chars[rand(0, $size - 1)];
			}
	
			return $str;
		}
		
		/*
		 *	Encode array into JSON
		*/
		private function json($data){
			if(is_array($data)){
				return json_encode($data);
			}
		}
	}
	
	// Initiate Library
	$api = new API;
	$api->processApi();
?>