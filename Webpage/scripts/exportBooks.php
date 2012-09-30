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


include ('../CONFIG.php');
//Connect to database
include ('../dbConnect.php');

//Get the key
$pKey = $_REQUEST['mykey'];

$mykey = _MY_KEY;


if ($pKey != $mykey)
    die();


printAllBooks();


function printAllBooks()
{


    $queryAllBooks = sprintf(
        "SELECT BI.isbn, BI.title, BI.authors, BI.publishedYear,
BI.pageCount, BI.dateOfInsert, 
BI.imgURL, BI.lang 
FROM SMARTLIB_BOOK_INFO BI 
;"
    );


    //Find Unique ID of Book Info
    $result = mysql_query($queryAllBooks) or dbError(mysql_error());


    /*if(!($_SESSION['web']==1)){

        $row_set[] = array(
                "result"=>"1",
        );
    }
    */
    while ($row = mysql_fetch_row($result)) //RM was assoc for no duplicate infos
    {
        //Each row, has 8 columns with data fetched from database
        //isbn, title, authors, publishedYear, pageCount, dateOfInsert, imgURL, lang
        echo "@isbn {" . $row[0] . ", <br>" .
            "title = {" . $row[1] . "}, <br>" .
            "author = {" . $row[2] . "}, <br>" .
            "publishedYear = {" . $row[3] . "}, <br>" .
            "pageCount = {" . $row[4] . "}, <br>" .
            "dateOfInsert = {" . $row[5] . "}, <br>" .
            //REMOVED img: "imgURL = {}, ".
            "lang = {" . $row[7] . "} } ,<br>";

        echo "<br>";
        //OLD..rm?
        $row_set[] = $row;
    }
//	echo json_encode($row_set);

}


// Sends error to mobile device: Book dont exists in Google API
/*function mobileSendWeirdError(){
	$result[] = array(
			"result"=>"-12"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}

*/


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

    //TODO SEND BIXTEXT ERROR!

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
