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
		
		private function addGame() {
			if($this->get_request_method() != "POST") {
				$this->response('', 406);
			}
			
			$auth_token = $this->_request['auth_token'];
			$status = $this->_request['status'];
			$waiting_for = $this->_request['waiting_for'];
						
			if(rand(0, 1) == 0) {
				$home_username = $this->_request['username'];
				$away_username = null;
			} else {
				$away_username = $this->_request['username'];
				$home_username = null;
			}
			
			if(isset($status)) {
				$stmt_auth = mysqli_prepare($this->db, 
										"SELECT * FROM users WHERE auth_token = ? LIMIT 1");
				mysqli_stmt_bind_param($stmt_auth, "s", $auth_token);
				mysqli_stmt_execute($stmt_auth);
				mysqli_stmt_store_result($stmt_auth);
				//$auth = mysqli_query($this->db, "SELECT * FROM users WHERE auth_token = '$auth_token' LIMIT 1");
				if(mysqli_stmt_num_rows($stmt_auth) == 1) {
					$stmt = mysqli_prepare($this->db, 
											"INSERT INTO games (status, waiting_for, home_username, away_username)
											VALUES (?, ?, ?, ?)");
					mysqli_stmt_bind_param($stmt, "ssss", $status, $waiting_for, $home_username, $away_username);
					$success = mysqli_stmt_execute($stmt);
					
					//$sql = mysql_query("INSERT INTO games (status, waiting_for, home_username, away_username) 
										//VALUES ('$status', '$waiting_for', '$home_username', '$away_username')", $this->db);
					if($success) {
						$id = mysqli_insert_id($this->db);
						$result = mysqli_query($this->db, "SELECT * FROM games WHERE id = '$id' LIMIT 1");
						if($result) {
							$row = mysqli_fetch_assoc($result);
							$this->response($this->json($row), 200);
						}
					} else if(!$success) {
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
			
			if(isset($username) && isset($auth_token)) {
				$stmt_auth = mysqli_prepare($this->db, 
										"SELECT * FROM users WHERE auth_token = ? LIMIT 1");
				mysqli_stmt_bind_param($stmt_auth, "s", $auth_token);
				mysqli_stmt_execute($stmt_auth);
				mysqli_stmt_store_result($stmt_auth);
				//$auth = mysqli_query($this->db, "SELECT * FROM users WHERE auth_token = '$auth_token' LIMIT 1");
				if(mysqli_stmt_num_rows($stmt_auth) == 1) {
					if(empty($status)) {
						$stmt = mysqli_prepare($this->db, 
												"SELECT * FROM games
												WHERE home_username = ? OR away_username = ? OR waiting_for = ?");
						mysqli_stmt_bind_param($stmt, "sss", $username, $username, $username);
						$result = mysqli_stmt_execute($stmt);
						
						//$sql = mysql_query("SELECT * FROM games 
								//WHERE home_username = '$username' OR away_username = '$username' OR waiting_for = '$username'", $this->db);
					} else {
						$stmt = mysqli_prepare($this->db,
												"SELECT * FROM games
												WHERE status = ? AND
												(home_username = ? OR away_username = ? OR waiting_for = ?)");
						mysqli_stmt_bind_param($stmt, "ssss", $status, $username, $username, $username);
						$result = mysqli_stmt_execute($stmt);
						
						//$sql = mysql_query("SELECT * FROM games 
							//	WHERE status = '$status' AND 
								//(home_username = '$username' OR away_username = '$username' OR waiting_for = '$username')", $this->db);
					}
					while($row = mysqli_fetch_assoc($result)) {
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
			$waiting_for = $this->_request['waiting_for'];			
			$home_score = $this->_request['home_score'];
			$away_score = $this->_request['away_score'];
			$stones_played = $this->_request['stones_played'];
			$home_username = $this->_request['home_username'];
			$away_username = $this->_request['away_username'];
				
			if(isset($auth_token) &&
			isset($id) &&
			isset($status) &&
			isset($home_username) &&
			isset($away_username)) {
				$stmt_auth = mysqli_prepare($this->db, 
										"SELECT * FROM users WHERE auth_token = ? LIMIT 1");
				mysqli_stmt_bind_param($stmt_auth, "s", $auth_token);
				mysqli_stmt_execute($stmt_auth);
				mysqli_stmt_store_result($stmt_auth);
				//$auth = mysqli_query($this->db, "SELECT * FROM users WHERE auth_token = '$auth_token' LIMIT 1");
				if(mysqli_stmt_num_rows($stmt_auth) == 1) {
					$stmt = mysqli_prepare($this->db, 
											"UPDATE games SET status = ?, waiting_for = ?, home_score = ?,
											away_score = ?, stones_played = ?, home_username = ?, away_username = ?
											WHERE id = ?");
					mysqli_stmt_bind_param($stmt, "ssiiissi", $status, $waiting_for, $home_score, $away_score, 
											$stones_played, $home_username, $away_username, $id);
					mysqli_stmt_execute($stmt);
												
					//$sql = mysql_query("UPDATE games SET status = '$status', waiting_for = '$waiting_for', home_score = '$home_score', 
						//	away_score = '$away_score', stones_played = '$stones_played', home_username = '$home_username', 
							//away_username = '$away_username' WHERE id = '$id'", $this->db);
					if(mysqli_affected_rows($this->db) > 0) {
						$res = mysqli_query($this->db, "SELECT modified FROM games WHERE id = '$id'");
						$array = mysqli_fetch_assoc($res);
						$this->response($this->json($array), 200);
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
			
			if(isset($id) && isset($auth_token)) {
				$stmt_auth = mysqli_prepare($this->db, 
										"SELECT * FROM users WHERE auth_token = ? LIMIT 1");
				mysqli_stmt_bind_param($stmt_auth, "s", $auth_token);
				mysqli_stmt_execute($stmt_auth);
				mysqli_stmt_store_result($stmt_auth);
				//$auth = mysqli_query($this->db, "SELECT * FROM users WHERE auth_token = '$auth_token' LIMIT 1");
				if(mysqli_stmt_num_rows($stmt_auth) == 1) {
					$stmt = mysqli_prepare($this->db, 
											"DELETE FROM games WHERE id = ?");
					mysqli_stmt_bind_param($stmt, "i", $id);
					mysqli_stmt_execute($stmt);
					
					//$sql = mysql_query("DELETE FROM games WHERE id = '$id'", $this->db);
					if(mysqli_affected_rows($this->db) > 0) {
						$success = array('status' => "Success", "msg" => "Successfully one record deleted.");
						$this->response($this->json($success), 200);
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