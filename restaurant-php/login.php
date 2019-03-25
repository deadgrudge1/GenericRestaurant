<?php

 	define('DB_HOST', 'localhost');
 	define('DB_USER', 'admin@restaurant');
 	define('DB_PASS', 'foodislife');
 	define('DB_NAME', 'restaurant');
 
	 //establish connection
 	$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
 

	if (mysqli_connect_errno()) 
	{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
		die();
	}

	$username = $POST["username"];
	$password = $POST["password"];
	$phone = $POST["phone"];

	$stmt = $conn->prepare("select ID_User from users_auth where UserName='".$username."' and PassWord='".$password."'");
 
 //executing the query 
 	
	if($stmt->execute())
	{
		$stmt->bind_result($id);
		$array = array();

		while($stmt->fetch())
		{
			$temp = array();
 			$temp['ID_User'] = $id; 
			array_push($array, $temp);
		}

		echo json_encode(array);
		echo "Login Successful";
	}
	else
	{
		echo "Login Failed";
	}
