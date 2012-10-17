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

include_once('genericFunctions.php');

//Get the key
$pKey = $_REQUEST['mykey'];
//Get username and password for user
$pUsername = $_REQUEST['user'];

$pType = $_REQUEST['type'];

$mykey = _MY_KEY;


if ($pKey != md5($mykey))
    die();

//Export User Books
if ($pUsername != '') {
    printUserBooks($pUsername, $pType);
} //Export AllBooks
else {
    printAllBooks($pType);
}


function printUserBooks($pUsername, $type)
{
    $device = "?device=web";
    $username = "&username=" . $pUsername;
    $mykey = "&mykey=" . _MY_KEY;

    $buildURL = _LIB_URL . "mobile/getUserBooks.php" . $device . $username . $mykey;

    $json = file_get_contents($buildURL, 0, null, null);

    $phpArray = json_decode($json);

    // Print books in JSON
    if ($type == "json") {
        echo $json;
    } //Print books in bibtex
    else {

        //Delete the first object (Info object: query result and book results number)
        unset($phpArray[0]);

        //Iterate over json objects
        foreach ($phpArray as $object => $jobj) {

            //Iterate over Json fields
            foreach ($jobj as $key => $value) {

                if ($key == 'isbn') {
                    echo "@isbn {" . $value . ",\n";
                } else if ($key == 'lang') {
                    echo "\tlang = {" . $value . "} \n} ,\n\n";
                } else if ($key != 'imgURL' && $key != 'status') { //Skip imgURL case
                    echo "\t" . $key . " = {" . $value . "},\n";
                }
            }
        }
    }


}


function printAllBooks($pType)
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

    if ($pType == "json") {

        while ($row = mysql_fetch_assoc($result)) {
            $row_set[] = $row;
        }
        echo json_encode($row_set);

    } else {
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

        }
    }


//

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
