<?php

	$username="smartscan";

	$password="UZaXYqXBdKX6crmq";

	$database="smartscan";

	

	//Connect to MySQL

	$con=mysql_connect("dbserver.cs.ucy.ac.cy:3306",$username,$password);

	

	//Select database

	if (!$con)

 	 {

  		die('Could not connect: ' . mysql_error());

  	 }



	mysql_select_db($database, $con);

	

	$result1=mysql_query("SELECT isbn,title,authors,image,rate

						  FROM BOOK

						  ORDER BY rate DESC

						  LIMIT 20");

						  

	$rows = array();

	while($r = mysql_fetch_array($result1)) {

		$rows[] = $r;

	}

		//echo $row['isbn'];



	//echo print_r($row);

	echo json_encode($rows);



?>

