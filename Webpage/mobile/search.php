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
$pUsername = $_POST['username'];
$lib_id = isset($_POST["libid"]) ? intval($_POST["libid"]) : 0;
$_SESSION['keyword']= $_POST['keyword'];
$_SESSION['column']= $_POST['column'];


$_SESSION['UserID']="";

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
	verifyUser($pUsername);


	//User wants specific search, but not puts keyword?
	if($_SESSION['keyword']=="")
	{
		mobileSendWeirdError();
	}

	printUserQuery($lib_id);
}





//Checks if books already exists for user
function verifyUser($pUser){


	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pUser));

	// Find Unique ID of User
	$result = mysql_query($queryFindUser) or dbError(mysql_error());
	$_SESSION['UserID'] = mysql_result($result, 0);


	
	if($_SESSION['UserID'] ==""){
		mobileSendWeirdError();
	}


}



function printUserQuery($lib_id){
	
	$escKeyword = mysql_real_escape_string($_SESSION['keyword']);
	
	$queryOfUser="";
	
	//Make a general query
	if($_SESSION['column'] =="")
	{
		$queryOfUser = sprintf("SELECT 
				U.username, 
				L.name as libname, 
				BI.isbn, BI.title, BI.authors, BI.publishedYear, BI.pageCount, BI.imgURL, BI.lang, 
				B.dateOfInsert, B.status,
				I.username as borrower 
			FROM SMARTLIB_BOOK_INFO BI 
				LEFT JOIN SMARTLIB_BOOK B ON BI.BI_ID = B.BI_ID 
				LEFT JOIN SMARTLIB_USER U ON B.U_ID=U.U_ID 
				LEFT JOIN SMARTLIB_LIBRARY L ON B.LIB_ID = L.ID 
				LEFT JOIN SMARTLIB_BORROWS R ON B.B_ID = R.B_ID 
				LEFT JOIN SMARTLIB_USER I ON R.U_ID = I.U_ID 
			WHERE (BI.isbn like '%s%s%s' OR BI.title like '%s%s%s' OR BI.authors like '%s%s%s') AND (L.ID = %d OR %d = 0)  
			ORDER BY BI.title",
			"%", $escKeyword, "%", 
			"%", $escKeyword, "%", 
			"%", $escKeyword, "%", 
			$lib_id,
			$lib_id);
	}
	else 
	{
		if(!($_SESSION['column'] == "isbn" || $_SESSION['column'] == "title" || $_SESSION['column'] == "authors"))
		{
			mobileSendWeirdError();
		}
		
		$queryOfUser = sprintf("SELECT 
				U.username, 
				L.name as libname, 
				BI.isbn, BI.title, BI.authors, BI.publishedYear, BI.pageCount, BI.imgURL, BI.lang, 
				B.dateOfInsert, B.status,
				I.username as borrower 
			FROM SMARTLIB_BOOK_INFO BI 
				LEFT JOIN SMARTLIB_BOOK B ON BI.BI_ID = B.BI_ID
				LEFT JOIN SMARTLIB_USER U ON B.U_ID = U.U_ID 
				LEFT JOIN SMARTLIB_LIBRARY L ON B.LIB_ID = L.ID 
				LEFT JOIN SMARTLIB_BORROWS R ON B.B_ID = R.B_ID 
				LEFT JOIN SMARTLIB_USER I ON R.U_ID = I.U_ID 
			WHERE BI.%s like '%s%s%s' AND (L.ID = %d OR %d = 0) ORDER BY BI.title", 
			$_SESSION['column'],
			"%", $escKeyword, "%",
			$lib_id,
			$lib_id);
	}

	//Run query TODO move outside
	$result = mysql_query($queryOfUser) or dbError(mysql_error());
	
	
	//Get book ID and its status
	if(($total =mysql_num_rows($result)) == 0){
		mobileSendUserHasNoResultsFound();
	}
	
	
	$row_set[] = array(
			"result"=>"1",
			"booksNum"=>"$total"
	);
	
	
	while($row = mysql_fetch_assoc($result))
	{
		$row_set[] = $row;
	}
	echo json_encode($row_set);
	
}




// Sends error to mobile device: Book dont exists in Google API
function mobileSendWeirdError(){
	$result[] = array(
			"result"=>"-12"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}


function mobileSendUserHasNoResultsFound(){
	$result[] = array(
			"result"=>"0"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}





// TODO Sends error to mobile device using JSON Object Format
function mobileSendDatabaseError(){
	$result[] = array(
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