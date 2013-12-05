<?php
	//CHECK commments below!
	//CHECK the arguments! this php is called with q=2!
	/*$page = $_GET['page']; // get the requested page
	$limit = $_GET['rows']; // get how many rows we want to have into the grid
	$sidx = $_GET['sidx']; // get index row - i.e. user click to sort
	$sord = $_GET['sord']; // get the direction

	if(!$sidx) 
		$sidx =1;*/
	
	
	
	// INFO connect to the database
	$username="smartscan";
	$password="UZaXYqXBdKX6crmq";
	$database="smartscan";//schema of the database


	$db = mysql_connect("dbserver.cs.ucy.ac.cy:3306",$username,$password)

	or die("Connection Error: " . mysql_error());
	
	//Select the database Schema
	mysql_select_db($database) or die("Error conecting to db."); 
	
/*
	//DELETE
	if($_GET[oper]=='sel')
	{
		echo "HII gabux";
 	if($_POST[oper]=='del') 
 		{
			
		echo "GABRIELA";
          $isbn = $_POST['isbn'];
		  $title = $_POST['title'];
		  $authors = $_POST['authors'];
		  $publishedYear = $_POST['publishedYear'];
		  $pageCount = $_POST['pageCount'];
		  
          $result = mysql_query("DELETE FROM USER_HAS_BOOK 
		  			 			 WHERE user_id='1' AND isbn=$isbn");
          $row = @mysql_query($result) or die("failed");
		  
		  echo $row;
		}
	}   */
	
	
	//If user wants to delete? CHECK
	//TODO add other operations here?
		//DELETE
 	if($_REQUEST[oper]=='del') 
 		{
          $isbn = $_REQUEST['isbn'];
		  $uid=$_REQUEST['uid'];
		  
		  $s = explode(",",$isbn);
		  
		 foreach ($s as $isbn1) {
   			//INFO Execute the query!
			 $result = mysql_query("DELETE
									FROM USER_HAS_BOOK
									WHERE isbn='$isbn1' AND user_id =(SELECT user_id
																	 FROM USER
																	 WHERE username='$uid')") or die("failed");
																	 
			 }
          
		  		
	
		}
	
      
 ?>