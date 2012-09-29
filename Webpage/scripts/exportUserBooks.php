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

include ('../CREDENCIALS.php');
include_once('genericFunctions.php');

//Get username and password for user
$pUsername = $_REQUEST['user'];
//$pUsername = $_SESSION['username'];
$pKey = $_REQUEST['mykey'];

$mykey = md5(_MY_KEY);

//9fef92839a85a6a8c657c35a641e37fb

if ($pKey != $mykey) {
    die();
}

//TODO this will be exported  URL for users export books!!!!


$device = "?device=web";
$username = "&username=" . $pUsername;
$mykey = "&mykey=" . _MY_KEY;

$buildURL = "http://" . getCustom2ndURL() . "mobile/getUserBooks.php" . $device . $username . $mykey;

$json = file_get_contents($buildURL, 0, null, null);

$phpArray = json_decode($json);

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

?>


<?php


//
//function authenticateUser($user, $pass){
//
//
//	require_once("../CREDENCIALS.php");
//	include ('../dbConnect.php');
//
//
//	$salt = _SALT;
//	$pepper = _PEPPER;
//	// Put salt and pepper
//	$pass = $salt.$pass.$pepper;
//
//	// Password Encryption
//	$pass = md5($pass);
//
//	// just to be sure.
//	$user = mysql_real_escape_string($user);
//
//	//Build the query string
//	$query = "SELECT * FROM SMARTLIB_USER WHERE username = '$user' AND password = '$pass'  LIMIT 1";
//
//	//Execute the query
//	$result = mysql_query($query) or die();
//
//
//
//	//Username is correct
//	while($row = mysql_fetch_array($result)){
//		$resusername = $row['username']; 			// username from DB
//		$respassword = $row['password']; 			// password from DB
//		$reslevel = $row['level']; 					// level from DB
//
//	}
//
//
//
//
//	// Found User in Database
//	if ($respassword == $pass){
//
//		//If user not activated or banned
//		if($reslevel<=0)
//			die();
//
//		//Else execution will continue, and books if any will printed in bibtex format
//
//	}
//	//Users credencials are wrong
//	else{
//		die();
//	}
//
//}

//
//	// CHECK CAN USE ALSO http://jsontoxml.utilities-online.info/
//function getCustom2ndURL(){
//
//	$len = strlen($_SERVER['REQUEST_URI']);
//	$fullURL=$_SERVER['REQUEST_URI'];
//	$found=0;
//
//
//	for( $i= $len-1; $i>0; $i--){
//
//
//		//Remove the last name of the URI
//		if($fullURL[$i]=="/"){
//
//			$found=1;
//
//			$urlResult = substr($fullURL,0,$i);
//			break;
//		}
//
//	}
//
//
//	$len = strlen($urlResult);
//	$fullURL=$urlResult;
//	$found=0;
//
//
//	for( $i= $len-1; $i>0; $i--){
//
//
//		//Remove the last name of the URI
//		if($fullURL[$i]=="/"){
//
//			$found=1;
//
//			$urlResult = substr($fullURL,0,$i+1);
//			break;
//		}
//
//	}
//
//
//
//	if(!$found)
//		$urlResult = $_SERVER['REQUEST_URI'];
//
//	return $_SERVER['SERVER_NAME']. $urlResult;
//
//}


?>