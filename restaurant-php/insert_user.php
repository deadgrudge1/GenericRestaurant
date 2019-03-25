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

	$stmt = $conn->prepare("insert into users_auth(UserName,PassWord,Phone) values('".$username."','".$password."','".$phone."')");
 
 //executing the query 
 	
	if($stmt->execute())
	{
		echo "Registered New User";
	}
	else
	{
		echo "New User Registration Failed";
	}


