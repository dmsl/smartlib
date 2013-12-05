<?php 

session_start();
$_SESSION['isMobileDevice']=0;

//Get the device, ISBN & Username
$device = $_POST['device'];


//Find out if we are on mobile device
if($device=="android" || $device=="iOS"){
	$_SESSION['isMobileDevice']=1;

}
//else{
//	//curently works only for mobile devices
//	die();
//}

//echo "is mobie:" .$_SESSION['isMobileDevice']." device: ".$_POST['device'];


//Connect to database
include ('../dbConnect.php');

if($_SESSION['isMobileDevice']){
//echo "test";

	//Inform user about his relation with the book
	printActivatedLibraries();

}


function printActivatedLibraries(){


	$libsQuery= sprintf("SELECT L.id, u.username as createdby, L.name, L.url, L.email, L.telephone, L.town, L.country FROM SMARTLIB_LIBRARY L left join SMARTLIB_USER u ON L.u_id = u.u_id WHERE activated='1' ORDER BY L.name");

	//Find Unique ID of Book Info
	

	$result = mysql_query($libsQuery) or dbError(mysql_error());

	
	
	$row_set[] = array(
			"result"=>"1"
	);
	
	while($row = mysql_fetch_assoc($result))
	{
		$row_set[] = $row;
	}


	echo json_encode($row_set);
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