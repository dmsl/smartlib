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

//Get the device, ISBN & Username Owner, & Username Destination
$device = $_POST['device'];
$pOwner = $_POST['owner'];//person who previously replied w/ "no" or still didt reply to request
$pUsername = $_POST['username'];//person who will delete his negative or not answered request
$pISBN = $_POST['isbn'];




//User owner, will lent this book to User Destination
$_SESSION['OwnerID']="";
$_SESSION['UsernameID']="";
//Book Info Data
$_SESSION['BookInfoID']="";
//Book Data
$_SESSION['BookID']="";




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






	if($pOwner==$pUsername){
		mobileSendWeirdError();
	}




	//Find out the relation between owner of book and the book
	findOwnersRelationWithBook($pOwner, $pISBN);

	//Find User ID of Destination User
	findUserIdOfRequester($pUsername);


	//Request this book from user Requester to user Owner

	deleteRequest();

	//Send Success Message
	mobileSendSuccessMessage();

}



// //Replies to request
function deleteRequest(){


	$queryFindAck= sprintf("SELECT R.acknowledge FROM SMARTLIB_REQUESTS R WHERE "
			." R.U_ID='%s' AND R.B_ID='%s'",
			mysql_real_escape_string($_SESSION['UsernameID']),
			mysql_real_escape_string($_SESSION['BookID']));


	// Find Book ACK information
	$result = mysql_query($queryFindAck) or dbError(mysql_error());
	$requestAck = mysql_result($result, 0);


	if($requestAck == ""){
		mobileSendWeirdError();
	}
	else if($requestAck == "1"){
		mobileSendWeirdError();
	}


	// 	//Insert into Requests table
	$queryLentStr= sprintf(
			"DELETE FROM SMARTLIB_REQUESTS WHERE U_ID='%s' AND B_ID='%s'",
			mysql_real_escape_string($_SESSION['UsernameID']),
			mysql_real_escape_string($_SESSION['BookID'])
	);


	mysql_query($queryLentStr) or dbError(mysql_error());

}


/** Finds User ID of User that requests the book
 *
 * */
function findUserIdOfRequester($pUsername){

	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pUsername));

	// Find Unique ID of User
	$result = mysql_query($queryFindUser) or dbError(mysql_error());
	$_SESSION['UsernameID'] = mysql_result($result, 0);

	if($_SESSION['UsernameID'] ==""){
		mobileSendWeirdError();
	}
}




/**
 * Finds user's relation with book
 * 1:
 * 0:
 * -1:
 * -2:
 * -3:
 * -11:
 * -12: Weird Error
 *
 * */
function findOwnersRelationWithBook($pOwner,$pISBN){

	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pOwner));

	// Find Unique ID of User
	$result = mysql_query($queryFindUser) or dbError(mysql_error());

	//Get book ID and its status
	if(mysql_num_rows($result) > 0){

		$row = mysql_fetch_row($result);

		$_SESSION['OwnerID'] = $row[0];

	}



	//User not found
	if($_SESSION['OwnerID'] ==""){
		mobileSendWeirdError();
	}




	//Find Unique ID of Book Info
	$result = mysql_query("SELECT BI_ID FROM SMARTLIB_BOOK_INFO WHERE isbn='".$pISBN."'") or dbError(mysql_error());

	$_SESSION['BookInfoID'] = mysql_result($result, 0);



	if($_SESSION['BookInfoID']==""){
		//User dont have this book (actually nobody in DB owns this book)
		mobileSendWeirdError();
	}



	//Find BookID and Its Status
	$sqlString ="SELECT B_ID, status FROM SMARTLIB_BOOK WHERE U_ID='".$_SESSION['OwnerID']."'".
			" AND BI_ID='".$_SESSION['BookInfoID']."'";


	$bookMatches = mysql_query($sqlString);


	

	//Get book ID and its status
	if(mysql_num_rows($bookMatches) > 0){

		$row = mysql_fetch_row($bookMatches);

		$_SESSION['BookID'] = $row[0];
	}
	//Use dont owns this book
	else{
		//User dont have this book
		mobileSendWeirdError();
	}


}




// Sends Success Message to Mobile Device
function mobileSendSuccessMessage(){
	$result = array(
			"result"=>"1"
	);
	//Encode Answer
	echo json_encode($result);

	die();
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
