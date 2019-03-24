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

 	$user_id = $_POST["ID_User"];
	$table_id = $POST["ID_Table"];
	$bill = $POST["Bill"];	
	$items_id = $POST["items_id"];
	$items_qty = $POST["items_qty"];

	$stmt = $conn->prepare("insert into orders(Time,ID_User,ID_Table,Bill,Status,Date) values(now(),".$user_id.",".table_id.",".bill.",0,now())");

	if($stmt->execute())
	{
		$stmt_order_id = $conn->prepare("select ID_Order from orders where ID_Table = 2 and Status = 0 order by ID_Order desc Limit 1
");

		$stmt_order_id->execute();
		$stmt_order_id->bind_result($order_id);
		
		for($i = 0; $i < sizeof($items_id); $i++)
		{
			$stmt_insert_item = $conn->prepare("insert into orders_items values(".$order_id.",".$items_id[$i].",".$items_qty[$i].")");

			$stmt_insert_item.execute();
		}

		echo "Order Placed";
	}
	else
	{
		echo "Order could not be placed";
	}

	
	
	
	


	
