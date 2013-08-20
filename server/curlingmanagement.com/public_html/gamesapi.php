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
			$username = $this->_request['username'];
			
			if(rand(0, 1) == 0) {
				$username_field = "home_username";
			} else {
				$username_field = "away_username";
			}
			
			if(!empty($status) && !empty($username)) {
				if(mysql_query("SELECT * FROM users WHERE auth_token = '$auth_token' LIMIT 1", $this->db)) {
					$sql = mysql_query("INSERT INTO games (status, " . $username_field . ") VALUES '$status', '$username'", $this->db);
					if($sql) {
						$id = mysql_insert_id($this->db);
						$this->response($this->json(array("id" => $id)), 200);
					} else if(!$sql) {
						$error = array('status' => "Failed", 'message' => "Game already exists");
						$this->response($this->json($error), 409);
					}
				} else {
					$this->response('', 401);
				}
			} else {
				// If invalid inputs "Bad Request" status message and reason
				$error = array('status' => "Failed", "msg" => "Bad parameters");
				$this->response($this->json($error), 400);
			}
		}
		
		private function getGames() {
			if($this->get_request_method() != "POST") {
				$this->response('', 406);
			}
			
			$username = $this->_request['username'];
			$status = $this->_request['status'];
			$auth_token = $this->_request['auth_token'];
			
			if(!empty($username) && !empty($auth_token)) {
				if(mysql_query("SELECT * FROM users WHERE auth_token = '$auth_token' LIMIT 1", $this->db)) {
					if(empty($status)) {
						$sql = mysql_query("SELECT * FROM games WHERE home_username = '$username' OR away_username = '$username'", $this->db);
					} else {
						$sql = mysql_query("SELECT * FROM games 
								WHERE status = '$status' AND (home_username = '$username' OR away_username = '$username')", $this->db);
					}
					while($row = mysql_fetch_assoc($sql)) {
						$results[] = $row;
					}
					$this->response($this->json($results), 200);
				} else {
					$this->response('', 401);
				}
			} else {
				// If invalid inputs "Bad Request" status message and reason
				$error = array('status' => "Failed", "msg" => "Bad parameters");
				$this->response($this->json($error), 400);				
			}
		}
		
		private function updateGame() {
			if($this->get_request_method() != "POST") {
				$this->response('', 406);
			}
			
			$auth_token = $this->_request['auth_token'];
			$id = $this->_request['id'];
			$status = $this->_request['status'];
			$home_score = $this->_request['home_score'];
			$away_score = $this->_request['away_score'];
			$stones_played = $this->_request['stones_played'];
			$home_username = $this->_request['home_username'];
			$away_username = $this->_request['away_username'];
				
			if(!empty($auth_token) &&
			!empty($id) &&
			!empty($status) &&
			!empty($home_username) &&
			!empty($away_username)) {
				if(mysql_query("SELECT * FROM users WHERE auth_token = '$auth_token'")) {
					$sql = mysql_query("UPDATE games SET status = '$status', home_score = '$home_score', away_score = '$away_score', 
							stones_played = '$stones_played', home_username = '$home_username', away_username = '$away_username'
							WHERE id = '$id'", $this->db);
					if(mysql_affected_rows() > 0) {
						$this->response('', 200);
					}
				} else {
					$this->response('', 401);
				}
			} else {
				// If invalid inputs "Bad Request" status message and reason
				$error = array('status' => "Failed", "msg" => "Bad parameters");
				$this->response($this->json($error), 400);				
			}
		}
		
		private function deleteGame() {
			if($this->get_request_method() != "POST") {
				$this->response('', 406);
			}
			
			$id = $this->_request['id'];
			$auth_token = $this->_request['auth_token'];
			
			if(!empty($id) && !empty($auth_token)) {
				if(mysql_query("SELECT * FROM users WHERE auth_token = '$auth_token'", $this->db)) {
					$sql = mysql_query("DELETE FROM games WHERE id = '$id'", $this->db);
					if(mysql_affected_rows() > 0) {
						$success = array('status' => "Success", "msg" => "Successfully one record deleted.");
						$this->response($this->json($success),200);
					}
				} else {
					$this->response('', 401);
				}
			} else {
				// If invalid inputs "Bad Request" status message and reason
				$error = array('status' => "Failed", "msg" => "Bad parameters");
				$this->response($this->json($error), 400);
			}			
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