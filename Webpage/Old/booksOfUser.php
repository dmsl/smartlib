<?php    

$username1 = $_GET['username'];

$page = $_GET['page']; // get the requested page

$limit = $_GET['rows']; // get how many rows we want to have into the grid

$sidx = $_GET['sidx']; // get index row - i.e. user click to sort

$sord = $_GET['sord']; // get the direction
//echo $limit;

if(!$sidx) $sidx =1;

//$username1 = $_SESSION['username'];

//$username1 = $_REQUEST['username'];

//$password1 = $_REQUEST['password'];


/************************************************************************************************************/

//SEARCH

//array to translate the search type

$ops = array(

		'eq'=>'=', //equal

		'ne'=>'<>',//not equal

		'lt'=>'<', //less than

		'le'=>'<=',//less than or equal

		'gt'=>'>', //greater than

		'ge'=>'>=',//greater than or equal

		'bw'=>'LIKE', //begins with

		'bn'=>'NOT LIKE', //doesn't begin with

		'in'=>'LIKE', //is in

		'ni'=>'NOT LIKE', //is not in

		'ew'=>'LIKE', //ends with

		'en'=>'NOT LIKE', //doesn't end with

		'cn'=>'LIKE', // contains

		'nc'=>'NOT LIKE'  //doesn't contain

);



/**

* Returns SQL WHERE clause

* @param string $col sql column name

* @param string $oper operator from jqGrid

* @param string $val value (right hand side)

*/

function getWhereClause($col, $oper, $val){

	global $ops;

	if($oper == 'bw' || $oper == 'bn') $val .= '%';

	if($oper == 'ew' || $oper == 'en' ) $val = '%'.$val;

	if($oper == 'cn' || $oper == 'nc' || $oper == 'in' || $oper == 'ni') $val = '%'.$val.'%';

	return " WHERE $col {$ops[$oper]} '$val' ";

}



$where = ""; //if there is no search request sent by jqgrid, $where should be empty

$searchField = isset($_GET['searchField']) ? $_GET['searchField'] : false;

$searchOper = isset($_GET['searchOper']) ? $_GET['searchOper']: false;

$searchString = isset($_GET['searchString']) ? $_GET['searchString'] : false;

if ($_GET['_search'] == 'true') {

	$where = getWhereClause($searchField,$searchOper,$searchString);

}



/************************************************************************************************************/



// connect to the database

$username="smartscan";

$password="UZaXYqXBdKX6crmq";

$database="smartscan";



$db = mysql_connect("dbserver.cs.ucy.ac.cy:3306",$username,$password)

or die("Connection Error: " . mysql_error());



mysql_select_db($database) or die("Error conecting to db.");

//echo $username1;
//echo $where;

$result = mysql_query("Select COUNT(*) AS count from

		(SELECT  B.image,B.isbn,B.title,B.authors,B.publishedYear,B.pageCount

		FROM USER U,USER_HAS_BOOK UHB,BOOK B
			
		WHERE U.username='$username1' AND U.user_id=UHB.user_id AND UHB.isbn=B.isbn)test
			
		$where");

$row = mysql_fetch_array($result,MYSQL_ASSOC);

$count = $row['count'];

//echo $row['count'];



if( $count >0 ) {

	$total_pages = ceil($count/$limit);

} else {

	$total_pages = 0;

}

if ($page > $total_pages) $page=$total_pages;

$start = $limit*$page - $limit; // do not put $limit*($page - 1)



if ($start < 0 ) {
	$start = 0;
}

//echo 'ORDER BY '.$sidx.' '.$sord.' LIMIT '.$start. ', ' .$limit;


$SQL = "Select * from

(SELECT  DISTINCT B.image, B.isbn,B.title,B.authors,B.publishedYear,B.pageCount

FROM USER U,USER_HAS_BOOK UHB,BOOK B

WHERE U.username='$username1' AND U.user_id=UHB.user_id AND UHB.isbn=B.isbn) test

$where

ORDER BY $sidx $sord LIMIT $start , $limit";

$result = mysql_query( $SQL ) or die("Couldn t execute query.".mysql_error());



$responce->page = $page;

$responce->total = $total_pages;

$responce->records = $count;

$i=0;

while($row = mysql_fetch_array($result,MYSQL_ASSOC)) {

	$responce->rows[$i]['isbn']=$row[isbn];

	$responce->rows[$i]['cell']=array($row[image],$row[isbn],$row[title],$row[authors],$row[publishedYear],$row[pageCount]);

	$i++;

}

echo json_encode($responce);

?>