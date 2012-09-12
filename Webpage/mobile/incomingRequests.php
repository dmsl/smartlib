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
$pUsername = $_POST['username'];//person who wants to take his requests


/*
* username got: from asks for my books
*
*  1: got requests. check number
*  0: no incoming requests
*/


//User owner, will lent this book to User Destination
$_SESSION['UsernameID']="";



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




	//Find out the relation between owner of book and the book
	//findOwnersRelationWithBook($pOwner, $pISBN);

	
	
	//Find User ID of Destination User
	findMyID($pUsername);


	getMyRequests();



}






/** Check if book already requested
 *
 * */ 
function getMyRequests(){
	

	/*

SELECT  
BI.isbn, BI.title, BI.authors, REQ_USER.username, R.date, R.acknowledge
FROM SMARTLIB_BOOK B, SMARTLIB_USER U, SMARTLIB_BOOK_INFO BI, 
SMARTLIB_REQUESTS R, SMARTLIB_USER REQ_USER
WHERE BI.BI_ID =B.BI_ID AND
U.U_ID=B.U_ID
AND R.B_ID=B.B_ID AND
REQ_USER.U_ID = R.U_ID
AND
U.U_ID='23'

	  
	 */

	$queryFindPrevRequest= sprintf("SELECT BI.isbn, BI.title, BI.authors, BI.imgURL, REQ_USER.username, "
			."R.date, R.acknowledge FROM SMARTLIB_BOOK B, SMARTLIB_USER U, SMARTLIB_BOOK_INFO BI, "
			." SMARTLIB_REQUESTS R, SMARTLIB_USER REQ_USER WHERE BI.BI_ID =B.BI_ID AND "
			." U.U_ID=B.U_ID AND R.B_ID=B.B_ID AND REQ_USER.U_ID = R.U_ID AND U.U_ID='%s' ORDER BY  R.acknowledge",
			mysql_real_escape_string($_SESSION['UsernameID'])
			);

	// Find Unique ID of User
	$result = mysql_query($queryFindPrevRequest) or dbError(mysql_error());

	
	//Get book ID and its status
	if(($total =mysql_num_rows($result)) == 0){
		mobileSendNoRequestSend();
	}
	
	
		//Send my requests to mobile
		// mysql_fetch_assoc
		
		$row_set[] = array(
				"result"=>"1",
				"booksNum"=>"$total"
		);
		
		
		
		
		
		while($row = mysql_fetch_assoc($result))
		{
			$row_set[] = $row;
		}
		echo json_encode($row_set);
		
		

	/*
	 
	 
	  $rows = array();
while($r = mysql_fetch_assoc($sqldata)) {
  $rows[] = $r;
}

echo json_encode($rows);
	 * */

}



/** Finds User ID of User that requests the book
 *
 * */
function findMyID($pUsername){

	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pUsername));

	// Find Unique ID of User
	$result = mysql_query($queryFindUser) or dbError(mysql_error());
	$_SESSION['UsernameID'] = mysql_result($result, 0);

	if($_SESSION['UsernameID'] ==""){
		mobileSendWeirdError();
	}
 ;
}





// Previously requested
function mobileSendNoRequestSend(){
	$row_set[] = array(
			"result"=>"0",
	);
	
	//Encode Answer
	echo json_encode($row_set);


	die();
}










// Sends error to mobile device: Book dont exists in Google API
function mobileSendWeirdError(){
	
	$row_set[] = array(
			"result"=>"-12",
	);
	
	//Encode Answer
	echo json_encode($row_set);

	die();
}






// TODO Sends error to mobile device using JSON Object Format
function mobileSendDatabaseError(){
	
	$row_set[] = array(
			"result"=>"-11",
	);
	
	//Encode Answer
	echo json_encode($row_set);

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