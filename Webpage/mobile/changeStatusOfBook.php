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
$pISBN = $_POST['isbn'];
$pOwner = $_POST['username'];
$pNewStatus = $_POST['newstatus'];

/*owner
 * isbn
* device
*
* 1: successfully returned book

* -11: Database Error
* -12: Weird Error (Programmers Error!!) <---
*
*
*
*RM chameno symbolo: 9789601420455
*
*/


//User owner, will lent this book to User Destination
$_SESSION['OwnerID']="";
//$_SESSION['DestinationID']="";
//Book Info Data
$_SESSION['BookInfoID']="";
//Book Data
$_SESSION['BookID']="";
$_SESSION['BookStatus']="";


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

	//Book already lented. Must be returned first
	if(!($pNewStatus=="0" || $pNewStatus =="-1" || $pNewStatus=="-2")){
		mobileSendWeirdError();
	}

	//Find out the relation between owner of bookand book
	findUsersRelationWithBook($pOwner, $pISBN);

	//If this book was already lented
	if($_SESSION['BookStatus']!=$pNewStatus){
		//Change the status
		changeBookStatus($pNewStatus);

		//Send Success Message
		mobileSendSuccessMessage();

	}
	//Weird Error . This should have been checked before
	else{
		mobileSendWeirdError();
	}







}

//Lents a Book from user owner to user Destination
function changeBookStatus($newStatus){

	//Mark book again as Available
	mysql_query("UPDATE SMARTLIB_BOOK SET status='".$newStatus."' WHERE B_ID='".$_SESSION['BookID']."'") or dbError(mysql_error());

	//If new status < 0 , autoreply to all Requests if exists to negative!
	if($newStatus<0){
		markAllBookRequestsToNO();
	}
}


//Mark all book requests to now
function markAllBookRequestsToNO(){

	//If it was answered,
	//	else if($requestAck != "-1"){
	//		return;
	//}


	// 	//Insert into Requests table
	$queryLentStr= sprintf(
			"UPDATE SMARTLIB_REQUESTS SET acknowledge='0' WHERE B_ID='%s'",
			mysql_real_escape_string($_SESSION['BookID'])
	);

	mysql_query($queryLentStr) or dbError(mysql_error());

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
function findUsersRelationWithBook($pOwner,$pISBN){


	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pOwner));

	// Find Unique ID of User
	$result = mysql_query($queryFindUser) or dbError(mysql_error());
	$_SESSION['OwnerID'] = mysql_result($result, 0);

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
		$_SESSION['BookStatus'] = $row[1];

		//If book is rented, dont allow user change anything!
		if($_SESSION['BookStatus'] == "1"){
			mobileSendWeirdError();
		}

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
		die();
	}
	else{
		//if there is DB Error, inform user and move him back
		inform($pError);


		if($_SESSION['currentPage']==""){
			$_SESSION['currentPage']="index.php";
		}

		header("Location: ".$_SESSION['currentPage']);
		die();
	}



}







?>