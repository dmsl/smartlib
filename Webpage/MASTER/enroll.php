<?php 
/**
 * THIS IS A PROPRIETARY FILE!
 * IS ONLY I SMARTLIB WEBPAGE!!!!!!!!!!!!!!!!
 *
 * */


session_start();

//Get the device, ISBN & Username
$name = $_POST['name'];
$url = $_POST['url'];
$email = $_POST['email'];
$telephone = $_POST['telephone'];
$town = $_POST['town'];
$country = $_POST['country'];



if($name=="" || $url=="" ||
		$email=="" ||
		$town=="" ||
		$country=="" ){
	error();
}

if(strlen($name)>50){
	echo "max lenght for name =50</br>";
	error();
}

if(strlen($url)>200){
	echo "max lenght for url =200</br>";
	error();
}
if(strlen($email)>200){
	echo "max lenght for email =200</br>";
	error();
}
if(strlen($telephone)>70){
	echo "max lenght for telephone =70</br>";
	error();
}
if(strlen($town)>70){
	echo "max lenght for town =70</br>";
	error();
}
if(strlen($country)>70){
	echo "max lenght for country =70</br>";
	error();
}



include ('../dbConnect.php');

$queryInsertStr= sprintf(
		"INSERT INTO LIBRARY (name, url, email, telephone, town,"
		."country) VALUES ('%s','%s','%s','%s','%s','%s')",
		mysql_real_escape_string($name),
		mysql_real_escape_string($url),
		mysql_real_escape_string($email),
		mysql_real_escape_string($telephone),
		mysql_real_escape_string($town),
		mysql_real_escape_string($country)
);


$insertLibrary = mysql_query($queryInsertStr) or error();


//Inform us about new library
// SMARTLIB ADMIN MAIL
$strTo =  "smartlib@cs.ucy.ac.cy";

$strHeader = "Content-type: text/html; charset=iso-8859-1\r\n";
$strHeader .= "From: New Library <smartlib@cs.ucy.ac.cy>";

//Build Email
$strSubject = "New library created ";
$strMessage = "New library with these details just created:<br>".
		"<br>name: " .$name.
		"<br>url: " .$url.
		"<br>email: " .$email.
		"<br>telephone: " .$telephone.
		"<br>town: " .$town.
		"<br>country: " .$country
		;



		//Send email
		// @ = avoid showing error
		$flgSend = @mail($strTo,$strSubject,$strMessage,$strHeader);


success();

//CHECK GOTO TO NEXT PAGE!
//header("Location: ".$_SESSION['currentPage']);
function success(){

	echo "Your library was successfully activated!!.</br>".
			"Please wait for its activation from Smartlib Administrator</br>";

			die();

}


function error(){

	echo "There was a database error.</br>".
			"Required fields: name, url, email,town, country</br>".
			"Also name and url must be unique within ALL libraries!</br>";


	die();

}

?>
