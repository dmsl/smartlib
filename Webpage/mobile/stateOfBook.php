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
$_SESSION['isMobileDevice']=0;

//Get the device, ISBN & Username
$device = $_POST['device'];
$pISBN = $_POST['isbn'];
$pUsername = $_POST['username'];

$_SESSION['UserID']="";
$_SESSION['BookInfoID']="";



//Find out if we are on mobile device
if($device=="android" || $device=="iOS"){
	$_SESSION['isMobileDevice']=1;

}
//else{
//	//curently works only for mobile devices
//	die();
//}

//Connect to database
include ('../dbConnect.php');

if($_SESSION['isMobileDevice']){

	//Inform user about his relation with the book
	sendUserStateOfBook($pUsername, $pISBN);

	/*
	 *  0: Available
	 *  1: Rented
	 * -1: No Rental
	 * -2: Other - No Rental
	 * -3: User dont owns it
	 * -12: Weird Error - Programmers Error
	 * -11: Database Error
	 * 
	 */
	

}





//Checks if books already exists for user
function sendUserStateOfBook($pUser,$pISBN){
	
	
	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pUser));

	// Find Unique ID of User
	$result = mysql_query($queryFindUser) or dbError(mysql_error());
	$_SESSION['UserID'] = mysql_result($result, 0);
	
	if($_SESSION['UserID'] ==""){
		mobileSendWeirdError();
	}

	
	//Find Unique ID of Book Info
	$result = mysql_query("SELECT BI_ID FROM SMARTLIB_BOOK_INFO WHERE isbn='".$pISBN."'") or dbError(mysql_error());

	$_SESSION['BookInfoID'] = mysql_result($result, 0);

	if($_SESSION['BookInfoID']==""){
		//User dont have this book
		mobileSendUserDontOwnsBook();
		
	}

	//Find if Book of user exists
	// Check username uniqueness
	$sqlString ="SELECT status FROM SMARTLIB_BOOK WHERE U_ID='".$_SESSION['UserID']."'".
			" AND BI_ID='".$_SESSION['BookInfoID']."'";


	$bookMatches = mysql_query($sqlString);
	
	

	

	$bookExists = mysql_num_rows($bookMatches);

	if($bookExists > 0){
		// Find Status Code of User's Book
		$_SESSION['status'] = mysql_result($bookMatches, 0);
		mobileSendBookOwnershipInfo($_SESSION['status']);

	}

	//User dont have this book
	mobileSendUserDontOwnsBook();
}





// Sends error to mobile device: Book dont exists in Google API
function mobileSendWeirdError(){
	$result = array(
			"result"=>"-12"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}


// Sends error to mobile device: Book dont exists in Google API
function mobileSendUserDontOwnsBook(){
	$result = array(
			"result"=>"-3"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}

// Sends Book Information to User
function mobileSendBookOwnershipInfo($statusCode){
	$result = array(
			"result"=>"$statusCode"
				);
	//Encode Answer
	echo json_encode($result);

	die();
}




// TODO Sends error to mobile device using JSON Object Format
function mobileSendDatabaseError(){
	$result = array(
			"result"=>"-11"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}








// Database Error
function dbError($pError){

	if($_SESSION['isMobileDevice']){
		//Inform Mobile Device about database Error
		mobileSendDatabaseError();
	}

	//if there is DB Error, inform user and move him back
	inform($pError);


	if($_SESSION['currentPage']==""){
		$_SESSION['currentPage']="index.php";
	}

	header("Location: ".$_SESSION['currentPage']);
	die();

}







?>