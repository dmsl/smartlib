<?php
//session_start();

//session_start();

echo 'this is session id from auth.php: ',$_GET['sess'],
'and this is session id in ????.php:',session_id(),
'and this is the print_r:<br><pre>';
print_r($_SESSION);

//Connect to database
include ('../dbConnect.php');

echo $_SESSION['currentPage']."cur page"
;

$_SESSION['isMobileDevice']=0;

//Get the device
$device = $_POST['device'];

//Get username and password from our form
$username = $_POST['username'];
$password = $_POST['password'];

//Find out if we are on mobile device
if($device=="android" || $device=="iOS"){
	$_SESSION['isMobileDevice']=1;
}



//echo "user :" . $username . "</br>"."pass: ".$password."</br>";

//Check if user hasnt provided credencials
if($username=="" || $password==""
		|| $username=="Type your Username"
		|| $password =="Type your Password"
){

	if($_SESSION['isMobileDevice']=="1")
		mobileSendLoginError();
	
	webSendLoginError();

}



//TODO this in register users also!
//TODO move this somewhere safer! Salting String
$salt = "5fG42Ak8^9bN";
$salt2 = "abcde07583";
// Put salt on password (salt+PASS+salt)
$salt .= $password.$salt2;
$password = $salt;

// Password Encryption
$password = md5($password);


// just to be sure.
$username = mysql_real_escape_string($username);

//Build the query string CHECK select * ?
$query = "SELECT * FROM SMARTLIB_USER WHERE username = '$username' AND password = '$password'  LIMIT 1";




//Execute the query(Find all users with that password)
$result = mysql_query($query) or dbError(mysql_error());



//Username is correct

while($row = mysql_fetch_array($result)){
	$resusername = $row['username']; 			// username from DB
	$respassword = $row['password']; 			// password from DB
	$resname = $row['name']; 					// users name from DB
	$ressurname = $row['surname']; 				// users surname from DB
	$resemail = $row['email']; 					// email from DB
	$restelephone= $row['telephone']; 			// telephone from DB
	$resallowRequests = $row['allowRequests']; 	// allowRequests from DB
	$reslevel = $row['level']; 					// level from DB

}



// Found User in Database
if ($respassword == $password)
{

	//If its a Mobile Device
	if($_SESSION['isMobileDevice']){
		
		//Send Login Info to Mobile Device
		mobileSendLoginData($resusername,$resname,$ressurname,
				$resemail,$restelephone,$resallowRequests,$reslevel);

	}
	//If its a regular PC
	else {

		$_SESSION['email'] = $resemail;
		$_SESSION['username'] = $resusername;
		$_SESSION['name'] = $resname;
		
		if($reslevel==0){
			$_SESSION['loggedin'] = "0";
			//header("Location: userNotActivated.php");
			$_SESSION['topTypeMsg'] = "err";
			$_SESSION['topMsg'] = $_SESSION['name']. ", your account is not activated</br>".
					"Activate it using your email: ".$_SESSION['email'];
            echo 	$_SESSION['topTypeMsg'].$_SESSION['topMsg'];
		}
			
		else if($reslevel==-1){
			$_SESSION['loggedin'] = "0";
			//header("Location: userNotActivated.php");
			$_SESSION['topTypeMsg'] = "err";
			$_SESSION['topMsg'] = $_SESSION['name']. ", you are a visitor to SmartLib.</br>".
					"Please activate full account.";
            echo 	$_SESSION['topTypeMsg'].$_SESSION['topMsg'];
		}

        else if($reslevel==-2){
            $_SESSION['loggedin'] = "0";
            //header("Location: userNotActivated.php");
            $_SESSION['topTypeMsg'] = "err";
            $_SESSION['topMsg'] = $_SESSION['name']. ", your are banned from SmartLib.</br>".
                "Contact SmartLib Admin for further Details.";
            echo 	$_SESSION['topTypeMsg'].$_SESSION['topMsg'];
        }

		else {
			$_SESSION['loggedin'] = "1";
			unset($_SESSION['topTypeMsg']);
			unset($_SESSION['topMsg']);
		}
		// TODO RM echo "conne". $_SESSION['currentPage'];
		//TODO
		//header("Location: ".$_SESSION['currentPage']);


	}

}
//Users credencials are wrong
else
{
	if($_SESSION['isMobileDevice']){
		mobileSendLoginError();
	}
	else{
		// Inform the user about wrong username or password
		$_SESSION['loggedin'] = "0";
		//header("Location: userNotActivated.php");
		$_SESSION['topTypeMsg'] = "err";
		$_SESSION['topMsg'] = "Wrong Username or Password.</br>".
				"Please try again.";

        echo 	$_SESSION['topTypeMsg'].$_SESSION['topMsg'];
		//TODO
		//header("Location: ".$_SESSION['currentPage']);
	}



}

//////////////////// Functions

//
function dbError($pError){

	if($_SESSION['isMobileDevice']){
		//Inform Mobile Device about database Error
		mobileSendDatabaseError();
	}


	if($_SESSION['currentPage']==""){
		$_SESSION['currentPage']="index.php";
	}


    $_SESSION['topTypeMsg']="err";
    $_SESSION['topMsg']="Internal Error: ".$pError."</br>You can't use SmartLib right now</br>".
        "Please check back later.";

    echo 	$_SESSION['topTypeMsg'].$_SESSION['topMsg'];

	//header("Location: ".$_SESSION['currentPage']);
	die();

}




//Send login data to mobile device
function mobileSendLoginData($user,$name,$surname,$email,$telephone,
		$allowRequests,$level){
	//Put Data in JSON Object
	//  $resemail; $resusername; $resname; $reslevel allowrequests

	$result = array(
			"result"=>"1",
			"username"=>"$user",
			"name"=>"$name",
			"surname"=>$surname,
			"email"=>$email,
			"telephone"=>$telephone,
			"allowRequests"=>$allowRequests,
			"level"=>$level
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





// Sends error to mobile device using JSON Object Format
function mobileSendLoginError(){
	$result = array(
			"result"=>"0"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}




//Sends login error to Website visitors
function webSendLoginError(){
	
	$_SESSION['topTypeMsg'] = "err";
	$_SESSION['topMsg'] = "Username or Password Cant be empty";
	
	if($_SESSION['currentPage']==""){
		$_SESSION['currentPage']="index.php";
	}

    echo 	$_SESSION['topTypeMsg'].$_SESSION['topMsg'];
	//header("Location: ".$_SESSION['currentPage']);
	die();
	
}


?>
</body>
</html>






