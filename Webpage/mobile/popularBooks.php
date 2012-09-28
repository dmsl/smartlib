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
//$pUsername = $_REQUEST['username'];
$pKey = $_REQUEST['mykey'];

$_SESSION['UserID'] = "";
$_SESSION['web'] = "0";

require_once("../CREDENCIALS.php");
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

    printPopularBooks();

}


//Checks if books already exists for user
/*
function findUserID($pUser){


	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pUser));

	// Find Unique ID of User
	$result = mysql_query($queryFindUser) or dbError(mysql_error());
	$_SESSION['UserID'] = mysql_result($result, 0);

	if($_SESSION['UserID'] ==""){
		mobileSendWeirdError();
	}

}

*/

function printPopularBooks()
{

    /*





 SELECT U.username, BI2.isbn, BI2.title, BI2.authors, BI2.publishedYear,
 BI2.pageCount, BI2.dateOfInsert,
 BI2.imgURL, BI2.lang, B2.status,
  count(*) AS CNT
 FROM SMARTLIB_USER U, SMARTLIB_BOOK_INFO BI2, SMARTLIB_BOOK B2,
 (
 (
 SELECT
 BI.BI_ID, BH.B_ID, BH.U_ID
 FROM SMARTLIB_BOOK B, SMARTLIB_BOOK_INFO BI, SMARTLIB_BORROW_HISTORY BH
 WHERE BI.BI_ID=B.BI_ID AND
 B.B_ID = BH.B_ID
 )
 UNION ALL
 (
 SELECT BI.BI_ID, BR.B_ID, BR.U_ID
 FROM SMARTLIB_BOOK B, SMARTLIB_BOOK_INFO BI, SMARTLIB_BORROWS BR
 WHERE BI.BI_ID=B.BI_ID AND
 B.B_ID = BR.B_ID
 )
 )
 as T2
 WHERE BI2.BI_ID=T2.BI_ID
 AND B2.B_ID=T2.B_ID AND B2.U_ID=U.U_ID

 GROUP BY T2.BI_ID
 ORDER BY COUNT(*) DESC
 ;



     * */

    $queryPopularBooksHugeQuery = sprintf(
        "SELECT U.username, BI2.isbn, BI2.title, BI2.authors, BI2.publishedYear,
BI2.pageCount, BI2.dateOfInsert,
BI2.imgURL, BI2.lang, B2.status,
 count(*) AS CNT
FROM SMARTLIB_USER U, SMARTLIB_BOOK_INFO BI2, SMARTLIB_BOOK B2,
(
(
-- Get Data from borrow history
SELECT
BI.BI_ID, BH.B_ID, BH.U_ID
FROM SMARTLIB_BOOK B, SMARTLIB_BOOK_INFO BI, SMARTLIB_BORROW_HISTORY BH
WHERE BI.BI_ID=B.BI_ID AND
B.B_ID = BH.B_ID
)
UNION DISTINCT
(
-- Get Data from Active Borrows
SELECT BI.BI_ID, BR.B_ID, BR.U_ID
FROM SMARTLIB_BOOK B, SMARTLIB_BOOK_INFO BI, SMARTLIB_BORROWS BR
WHERE BI.BI_ID=B.BI_ID AND
B.B_ID = BR.B_ID
)

UNION DISTINCT
(
-- Get Latest book aditions
SELECT BI.BI_ID, B.B_ID ,B.U_ID
FROM SMARTLIB_BOOK B, SMARTLIB_BOOK_INFO BI
WHERE BI.BI_ID=B.BI_ID ORDER BY BI.dateOfInsert LIMIT 20
)

)
as T2
WHERE BI2.BI_ID=T2.BI_ID
AND B2.B_ID=T2.B_ID AND B2.U_ID=U.U_ID

GROUP BY T2.BI_ID
ORDER BY COUNT(*) DESC
;"
    );


    //Find Unique ID of Book Info
    $result = mysql_query($queryPopularBooksHugeQuery) or dbError(mysql_error());


    //Get book ID and its status
    if (($total = mysql_num_rows($result)) == 0) {
        mobileSendNoPopularBooks();
    }

    if (!($_SESSION['web'] == 1)) {

        $row_set[] = array(
            "result" => "1",
        );
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
