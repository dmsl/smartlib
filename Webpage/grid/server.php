
 <?php

/*
 This file is part of SmartLib Project.
 
 SmartLib is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 SmartLib is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.
 
 
 Author: Paschalis Mpeis
 
 Affiliation:
 Data Management Systems Laboratory
 Dept. of Computer Science
 University of Cyprus
 P.O. Box 20537
 1678 Nicosia, CYPRUS
 Web: http://dmsl.cs.ucy.ac.cy/
 Email: dmsl@cs.ucy.ac.cy
 Tel: +357-22-892755
 Fax: +357-22-892701
 
 
 */
 
session_start();
	//Connect to SmartLib
	include("../dbConnect.php");
	
	

// coment the above lines if php 5
//include("JSON.php");
//$json = new Services_JSON();
// end comment
$examp = $_REQUEST["q"]; //query number

$page = $_REQUEST['page']; // get the requested page
$limit = $_REQUEST['rows']; // get how many rows we want to have into the grid
$sidx = $_REQUEST['sidx']; // get index row - i.e. user click to sort
$sord = $_REQUEST['sord']; // get the direction
if(!$sidx) $sidx =1;

$wh = "";
$searchOn = Strip($_REQUEST['_search']);
if($searchOn=='true') {
	$sarr = Strip($_REQUEST);
	foreach( $sarr as $k=>$v) {
		switch ($k) {
			case 'id':
			case 'isbn':
			case 'authors':
			case 'title':
				$wh .= " AND ".$k." LIKE '".$v."%'";
				break;
			case 'amount':
			case 'tax':
			case 'total':
				$wh .= " AND ".$k." = ".$v;
				break;
		}
	}
}

	
	

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

	



	switch($examp){
				case 'userbooks':
			$result = mysql_query("Select COUNT(*) AS count
			FROM SMARTLIB_BOOK_INFO BI, SMARTLIB_USER U, SMARTLIB_BOOK B 
			WHERE BI.BI_ID=B.BI_ID AND B.U_ID=U.U_ID AND U.username='" 
			.$_SESSION['username']."' ".$wh
			
			);
		
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
			
		
			
			if ($start < 0 ) {$start = 0;}
			
		
		
		
		$SQL = "SELECT  BI.imgURL, BI.title, BI.authors, BI.publishedYear, BI.pageCount, BI.lang,
						BI.isbn, BI.BI_ID, B.status, B.B_ID
		FROM SMARTLIB_BOOK_INFO BI, SMARTLIB_USER U, SMARTLIB_BOOK B 
		WHERE BI.BI_ID=B.BI_ID AND B.U_ID=U.U_ID AND U.username='"
		.$_SESSION['username']."' $wh ORDER BY $sidx $sord LIMIT $start , $limit";

		$result = mysql_query( $SQL ) or die("Couldn t execute query.".mysql_error());
		
				
				
				$responce->page = $page;
				
				$responce->total = $total_pages;
				
				$responce->records = $count;
		
				$i=0;
				while($row = mysql_fetch_array($result,MYSQL_ASSOC)) {
				 $responce->rows[$i]['B_ID']=$row[B_ID];
				 $responce->rows[$i]['cell']=array($row[imgURL],$row[title],$row[authors],$row[publishedYear]
				 ,$row[pageCount],$row[lang],$row[status],$row[isbn],$row[BI_ID],$row[B_ID]);
				 $i++;
				}
				
				
				
				echo json_encode($responce);
		break;
		
		
		
		//All books of library
		case 'allbooks':
			$result = mysql_query("Select COUNT(*) AS count FROM SMARTLIB_BOOK_INFO");
		
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
			
		
			
			if ($start < 0 ) {$start = 0;}
			
		
		
		
		$SQL = "SELECT BI.imgURL, BI.title, BI.authors, BI.publishedYear, BI.pageCount, 
		BI.lang, BI.isbn FROM SMARTLIB_BOOK_INFO BI ORDER BY $sidx $sord LIMIT $start , $limit";

		$result = mysql_query( $SQL ) or die("Couldn t execute query.".mysql_error());
		
				
				
				$responce->page = $page;
				
				$responce->total = $total_pages;
				
				$responce->records = $count;
		
				$i=0;
				while($row = mysql_fetch_array($result,MYSQL_ASSOC)) {
				 $responce->rows[$i]['isbn']=$row[isbn];
				 $responce->rows[$i]['cell']=array($row[imgURL],$row[title],$row[authors],$row[publishedYear]
				 ,$row[pageCount],$row[lang],$row[isbn]);
				 $i++;
				}
				
				
				
				echo json_encode($responce);
		break;
		
		
		
		
		
		
		
		
		//All books of library When user is logged in
		case 'allbooksloggedin':
			$result = mysql_query("Select COUNT(*) AS count FROM SMARTLIB_BOOK ".$wh);
		
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
			
		
			
			if ($start < 0 ) {$start = 0;}
			
		
		
		
		$SQL = "SELECT BI.imgURL, BI.title, BI.authors, BI.publishedYear, BI.pageCount, U.username,
		B.status,
		BI.lang, BI.isbn, B.B_ID
		 FROM
		SMARTLIB_BOOK_INFO BI, SMARTLIB_USER U, SMARTLIB_BOOK B
		WHERE BI.BI_ID=B.BI_ID AND B.U_ID=U.U_ID $wh
		ORDER BY $sidx $sord LIMIT $start , $limit";

		$result = mysql_query( $SQL ) or die("Couldn t execute query.".mysql_error());
		
				
				
				$responce->page = $page;
				
				$responce->total = $total_pages;
				
				$responce->records = $count;
		
				$i=0;
				while($row = mysql_fetch_array($result,MYSQL_ASSOC)) {
				 $responce->rows[$i]['B_ID']=$row[B_ID];
				 $responce->rows[$i]['cell']=array($row[imgURL],$row[title],$row[authors],$row[publishedYear]
				 ,$row[pageCount],$row[username],$row[status]
				 ,$row[lang],$row[isbn],$row[B_ID]);
				 $i++;
				}
				
				
				
				echo json_encode($responce);
		break;
		
		
		}


function Strip($value)
{
	if(get_magic_quotes_gpc() != 0)
  	{
    	if(is_array($value))  
			if ( array_is_associative($value) )
			{
				foreach( $value as $k=>$v)
					$tmp_val[$k] = stripslashes($v);
				$value = $tmp_val; 
			}				
			else  
				for($j = 0; $j < sizeof($value); $j++)
        			$value[$j] = stripslashes($value[$j]);
		else
			$value = stripslashes($value);
	}
	return $value;
}
function array_is_associative ($array)
{
    if ( is_array($array) && ! empty($array) )
    {
        for ( $iterator = count($array) - 1; $iterator; $iterator-- )
        {
            if ( ! array_key_exists($iterator, $array) ) { return true; }
        }
        return ! array_key_exists(0, $array);
    }
    return false;
}
?>