<?php
	
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
			$this->db = mysql_connect(self::DB_SERVER,self::DB_USER,self::DB_PASSWORD);
			if($this->db)
				mysql_select_db(self::DB,$this->db);
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
		
		private function getFieldsString() {
			$str = "(";
			if(array_key_exists('status', $this->_request)) {
				$str .= "status,";
			}
			if(array_key_exists('home_score', $this->_request)) {
				$str .= "home_score,";
			}
			if(array_key_exists('away_score', $this->_request)) {
				$str .= "away_score,";
			}
			if(array_key_exists('stones_played', $this->_request)) {
				$str .= "stones_played,";
			}			
			if(array_key_exists('username', $this->_request)) {
				if(rand(0, 1) == 0) {
					$str .= "home_username,";
				} else {
					$str .= "away_username,";
				}
			}
			
			$str = substr_replace($str, "", -1);
			
			$str .= ")";
			
			echo $str . '<br>';
			
			return $str;			
		}
		
		private function getValuesString() {
			$str = "(";
			if(array_key_exists('status', $this->_request)) {
				$status = $this->_request['status'];
				$str .= "'" . $status . "'";
				$str .= ",";
			}
			if(array_key_exists('home_score', $this->_request)) {
				$home_score = $this->_request['home_score'];
				$str .= $home_score;
				$str .= ",";
			}
			if(array_key_exists('away_score', $this->_request)) {
				$away_score = $this->_request['away_score'];
				$str .= $away_score;
				$str .= ",";
			}
			if(array_key_exists('stones_played', $this->_request)) {
				$stones_played = $this->_request['stones_played'];
				$str .= $stones_played;
				$str .= ",";
			}
			if(array_key_exists('username', $this->_request)) {
				$username = $this->_request['username'];
				$str .= "'" . $username . "'";
				$str .= ",";
			}
			
			if(array_key_exists('home_username', $this->_request)) {
				$home_username = $this->_request['home_username'];
				$str .= "'" . $home_username . "'";
				$str .= ",";
			}
			
			if(array_key_exists('away_username', $this->_request)) {
				$away_username = $this->_request['away_username'];
				$str .= "'" . $away_username . "'";
				$str .= ",";
			}
				
			$str = substr_replace($str ,"",-1);
				
			$str .= ")";
				
			echo $str . '<br>';
			
			return $str;
		}
		
		private function addGame() {
			if($this->get_request_method() != "POST") {
				$this->response('', 406);
			}
			
			$status = $this->_request['status'];
			$home_score->_request['home_score'];
			
			$line = "INSERT INTO games " . $this->getFieldsString() . " VALUES " . $this->getValuesString();
			
			echo $line . '<br>';
				
			if(!empty($this->_request)) {
				$sql = mysql_query("INSERT INTO games " . $this->getFieldsString() . " VALUES " . $this->getValuesString(), $this->db);
				if($sql) {
					$this->response('', 200);
				} else if(!$sql) {
					$error = array('status' => "Failed", 'message' => "Game already exists");
					$this->response($this->json($error), 409);
				}
			} else {
				// If invalid inputs "Bad Request" status message and reason
				$error = array('status' => "Failed", "msg" => "No parameters");
				$this->response($this->json($error), 400);
			}
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
			
			if(empty($email)) {
				$email = NULL;
			}
			
			if(!filter_var($email, FILTER_VALIDATE_EMAIL) && !empty($email)) {
				$error = array('status' => "Forbidden", "msg" => "Not a valid email address");
				$this->response($this->json($error), 403);
			}
			// Input validations
			if(!empty($username) and !empty($password)) {
				$sql = mysql_query("INSERT INTO users (username, password, email) 
									VALUES ('$username', '$password', '$email')", $this->db);
				/*$sql = mysql_query("INSERT INTO users (username, password, email) VALUES ('$username', '$password', '$email')
									IF NOT(SELECT * FROM users WHERE username == '$username')", $this->db);*/
				if($sql) {
					$this->response('', 200);
				} else if(!$sql) {
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
			
			if(empty($email)) {
				$email = NULL;
			}
			
			if(!filter_var($email, FILTER_VALIDATE_EMAIL) && !empty($email)) {
				$error = array('status' => "Forbidden", "msg" => "Not a valid email address");
				$this->response($this->json($error), 403);
			}
				
			// Input validations
			if(!empty($username) and !empty($password)) {
				$sql = mysql_query("UPDATE users SET password = '$password', email = '$email'
						WHERE username = '$username'", $this->db);
				if(mysql_affected_rows() > 0){
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
			
			if(!empty($username) and !empty($password)) {
				mysql_query("DELETE FROM users WHERE username = '$username'", $this->db);
				if(mysql_affected_rows() > 0) {
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