<!-- WILL BE REMOVED! -->
<?php

//Selected Database
$database="smartscan";
//Encrypted Password TODO
$db_password="UZaXYqXBdKX6crmq";

//get the rating of user and the isbn of book that is rated
$rating = $_REQUEST['rating'];
$book = $_REQUEST['book'];

//INFO: How to Connect to Database
$connect = mysql_connect("dbserver.cs.ucy.ac.cy:3306",$database,$db_password);

//Failed to connect to database
if(!$connect)
{
	die("Connection Error: " . mysql_error());
}

//Select the Schema (Same as Database username)
mysql_select_db($database) or die("Error conecting to db.");

//INFO HOWTO: update sumRate and Count
// INFO a query is executing!!!
mysql_query("UPDATE BOOK
		SET countRated=countRated+1,sumRate=sumRate+'$rating',rate=sumRate/countRated
		WHERE isbn='$book'");
//INFO with NEW query, GET THE results!!!
$newrating=mysql_query("SELECT countRated,ROUND(rate)
		FROM BOOK
		WHERE isbn='$book';");

?>