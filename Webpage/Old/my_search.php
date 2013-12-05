<!-- CHECK my_search
IT SEEMS THAT: my_search searches ONLY between users books!
shows books of user? 
search_after_login, search, my_search
-->

<?php
$username1 = $_GET['username'];
	
	$page = $_GET['page']; // get the requested page

	$limit = $_GET['rows']; // get how many rows we want to have into the grid

	//$sidx = $_GET['sidx']; // get index row - i.e. user click to sort

	//$sord = $_GET['sord']; // get the direction

	//if(!$sidx) $sidx =1;


	$searchString = isset($_GET['searchString']) ? $_GET['searchString'] : false;


	// connect to the database TODO this AIO function!
	$username="smartscan";
	$password="UZaXYqXBdKX6crmq";
	$database="smartscan";


	$db = mysql_connect("dbserver.cs.ucy.ac.cy:3306",$username,$password)

	or die("Connection Error: " . mysql_error());

	mysql_select_db($database) or die("Error conecting to db.");

	
	//CHECK what we saved in result?
	$result = mysql_query("Select COUNT(*) AS count FROM

						  (SELECT  B.image,B.isbn,B.title,B.authors,B.publishedYear,B.pageCount
     				 
					 	   FROM BOOK B,USER_HAS_BOOK UHB,USER U 
		  
		  				   WHERE U.username='$username1' AND UHB.user_id=U.user_id AND UHB.isbn=B.isbn) test

		WHERE  isbn LIKE '%$searchString%' OR title LIKE '%$searchString%' OR authors LIKE '%$searchString%' OR publishedYear LIKE '%$searchString%' OR pageCount LIKE '%$searchString%'");
							
			

	$row = mysql_fetch_array($result,MYSQL_ASSOC);
	//Save the number of results
	$count = $row['count'];

	//echo $row['count'];


	//if results are more than zero, find the number of total pages!
	if( $count >0 ) {
		$total_pages = ceil($count/$limit);
	//No results found
	} else {
		$total_pages = 0;
	}
//CHECK
if ($page > $total_pages) $page=$total_pages;

$start = $limit*$page - $limit; // do not put $limit*($page - 1)


if ($start < 0 ) {$start = 0;}


//Build the Query String
//CHECK why we have a new query?????
$SQL = "SELECT * FROM

	 (SELECT  B.image,B.isbn,B.title,B.authors,B.publishedYear,B.pageCount
     				 
	  FROM BOOK B,USER_HAS_BOOK UHB,USER U 
		  
	  WHERE U.username='$username1' AND UHB.user_id=U.user_id AND UHB.isbn=B.isbn) test

	  WHERE isbn LIKE '%$searchString%' OR title LIKE '%$searchString%' OR authors LIKE '%$searchString%' OR publishedYear LIKE '%$searchString%' OR pageCount LIKE '%$searchString%'

		LIMIT $start , $limit";
		
//Execute the Query
$result = mysql_query( $SQL ) or die("Couldn t execute query.".mysql_error());



$responce->page = $page;

$responce->total = $total_pages;

$responce->records = $count;

$i=0;
//Save all rows! Each time fetch a row from the result!
while($row = mysql_fetch_array($result,MYSQL_ASSOC)) {

    $responce->rows[$i]['isbn']=$row[isbn];

 $responce->rows[$i]['cell']=array($row[image],$row[isbn],$row[title],$row[authors],$row[publishedYear],$row[pageCount]);

    $i++;

}        
//CHECK Encode the responce???
echo json_encode($responce);


?>