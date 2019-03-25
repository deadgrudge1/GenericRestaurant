<?php 

 define('DB_HOST', 'localhost');
 define('DB_USER', 'admin@restaurant');
 define('DB_PASS', 'foodislife');
 define('DB_NAME', 'restaurant');
 
 //establish connection
 $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
 
 if (mysqli_connect_errno()) {
 echo "Failed to connect to MySQL: " . mysqli_connect_error();
 die();
 }
 
 //prepare query
 $stmt = $conn->prepare("select * from menu;");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($id, $name, $type, $image, $cost);
 
 $menu = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['ID_Menu'] = $id; 
 $temp['Name_Menu'] = $name; 
 $temp['Type_Menu'] = $type; 
 $temp['Image_Menu'] = $image; 
 $temp['Cost'] = $cost;  
 array_push($menu, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($menu);
