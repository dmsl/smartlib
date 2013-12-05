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
$_SESSION['isMobileDevice'] = 0;

//Get the device, ISBN & Username
$device = $_REQUEST['device'];
$lib_id = isset($_REQUEST["libid"]) ? intval($_REQUEST["libid"]) : 0;


//$pUsername = $_REQUEST['username'];
$pKey = $_REQUEST['mykey'];

$_SESSION['UserID'] = "";
$_SESSION['web'] = "0";

require_once("../CONFIG.php");
$mykey = _MY_KEY;

//Find out if we are on mobile device
if ($device == "android" || $device == "iOS" || $device == "web") {


    //If its web, enable web mode and make a key check
    if ($device == "web") {
        if ($pKey != $mykey)
            die();

        $_SESSION['web'] = 1;
    }

    $_SESSION['isMobileDevice'] = 1;

}
//else{
//	//curently works only for mobile devices
//	die();
//}


//Connect to database
include ('../dbConnect.php');

if ($_SESSION['isMobileDevice']) {


    // removed safety check
    //if($device!="web"){
    //Safety check
    //	findUserID($pUsername);
    //}

    printPopularBooks($lib_id);

}


function printPopularBooks($lib_id)
{
    $queryPopularBooksHugeQuery = sprintf("SELECT 
			U.username, 
			I.username as borrower, 
			BI.isbn, BI.title, BI.authors, BI.publishedYear, BI.pageCount, BI.dateOfInsert, BI.imgURL, BI.lang, 
			B.status
		FROM SMARTLIB_USER U 
			LEFT JOIN SMARTLIB_BOOK B ON B.U_ID = U.U_ID 
			LEFT JOIN SMARTLIB_LIBRARY L ON B.LIB_ID = L.ID 
			LEFT JOIN SMARTLIB_BOOK_INFO BI ON B.BI_ID = BI.BI_ID 
			LEFT JOIN SMARTLIB_BORROWS R ON R.B_ID = B.B_ID 
			LEFT JOIN SMARTLIB_USER I ON R.U_ID = I.U_ID 
		WHERE L.ID = %d 
		ORDER BY B.dateOfInsert DESC LIMIT 50", 
		$lib_id
    );
	
	#


    //Find Unique ID of Book Info
    $result = mysql_query($queryPopularBooksHugeQuery) or dbError(mysql_error());


    //Get book ID and its status
    if (($total = mysql_num_rows($result)) == 0) {
        mobileSendNoPopularBooks();
    }

    if (!($_SESSION['web'] == 1)) {

        $row_set[] = array("result" => "1");
    }

    while ($row = mysql_fetch_assoc($result)) {
        $row_set[] = $row;
    }
    echo json_encode($row_set);

}


// Sends error to mobile device: Book dont exists in Google API
function mobileSendWeirdError()
{
    $result[] = array(
        "result" => "-12"
    );
    //Encode Answer
    echo json_encode($result);

    die();
}


function mobileSendNoPopularBooks()
{
    $result[] = array(
        "result" => "0"
    );
    //Encode Answer
    echo json_encode($result);

    die();
}


// TODO Sends error to mobile device using JSON Object Format
function mobileSendDatabaseError()
{
    $result[] = array(
        "result" => "-11"
    );
    //Encode Answer
    echo json_encode($result);

    die();
}


// Database Error
function dbError($pError)
{

    if ($_SESSION['isMobileDevice']) {
        //Inform Mobile Device about database Error
        mobileSendDatabaseError();
    }

    //if there is DB Error, inform user and move him back
    inform($pError);


    if ($_SESSION['currentPage'] == "") {
        $_SESSION['currentPage'] = "index.php";
    }

    header("Location: " . $_SESSION['currentPage']);
    die();

}


?>
