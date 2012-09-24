<?php

Header('Content-type: text/plain');

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


//CHECK FOR MOBILE
session_start();
require_once("../CREDENCIALS.php");

//Connect to database
include ('../dbConnect.php');


$_SESSION['isMobileDevice'] = 0;

//Get the device
$device = $_POST['device'];

//Get username and password from our form (login.php)
$username = $_POST['username'];
$password = $_POST['password'];

//Find out if we are on mobile device
if ($device == "android" || $device == "iOS") {
    $_SESSION['isMobileDevice'] = 1;
}


//Check if user hasnt provided credencials
if ($username == "" || $password == ""
    || $username == "Type your Username"
    || $password == "Type your Password"
) {

    if ($_SESSION['isMobileDevice'] == "1")
        mobileSendLoginError();

    webSendLoginError();

}

$salt = _SALT;
$pepper = _PEPPER;
// Put salt and pepper
$password = $salt . $password . $pepper;

// Password Encryption
$password = md5($password);

// just to be sure.
$username = mysql_real_escape_string($username);

//Build the query string CHECK select * ?
$query = "SELECT * FROM SMARTLIB_USER WHERE username = '$username' AND password = '$password'  LIMIT 1";


//Execute the query(Find all users with that password)
$result = mysql_query($query) or dbError(mysql_error());


//Username is correct

while ($row = mysql_fetch_array($result)) {
    $resusername = $row['username']; // username from DB
    $respassword = $row['password']; // password from DB
    $resname = $row['name']; // users name from DB
    $ressurname = $row['surname']; // users surname from DB
    $resemail = $row['email']; // email from DB
    $restelephone = $row['telephone']; // telephone from DB
    $resallowRequests = $row['allowRequests']; // allowRequests from DB
    $reslevel = $row['level']; // level from DB

}


// Found User in Database
if ($respassword == $password) {

    //If its a Mobile Device
//    if ($_SESSION['isMobileDevice']) {


    //Mark session as logged in on Website
    if (!$_SESSION['isMobileDevice']) {
        if($reslevel>=1)
            $_SESSION['loggedin'] = "1";
        else
            $_SESSION['loggedin'] = "0";
    }


        //Send Login Info to Mobile Device
        mobileSendLoginData($resusername, $resname, $ressurname,
            $resemail, $restelephone, $resallowRequests, $reslevel);

//    } //If its a regular PC


//    else {
//
//        $_SESSION['email'] = $resemail;
//        $_SESSION['username'] = $resusername;
//        $_SESSION['name'] = $resname;
//
//        if ($reslevel == 0) {
//            $_SESSION['loggedin'] = "0";
//            //header("Location: userNotActivated.php");
//            //TODO RM TOP TYPE MESSAGE AND TOP MSG
//            $_SESSION['topTypeMsg'] = "err";
//            $_SESSION['topMsg'] = $_SESSION['name'] . ", your account is not activated</br>" .
//                "Activate it using your email: " . $_SESSION['email'];
//
//
//            $result = array(
//                "result" => "0",
//                "message" => $_SESSION['name'] . ", your account is not activated</br>" .
//                    "Activate it using your email: " . $_SESSION['email']
//            );
//
//            //Encode Answer
//            echo json_encode($result);
//            die();
//
//            //Show message to desktop user
//            //echo  $_SESSION['topTypeMsg'].$_SESSION['topMsg'];
//
//        } //User is Visitor
//        else if ($reslevel == -1) {
//            $_SESSION['loggedin'] = "0";
//            //TODO RM
//            //header("Location: userNotActivated.php");
//            $_SESSION['topTypeMsg'] = "err";
//            $_SESSION['topMsg'] = $_SESSION['name'] . ", your are a visitor to SmartLib.</br>" .
//                "Please make a regular account.";
//
//
//            $result = array(
//                "result" => "0",
//                "message" => $_SESSION['name'] . ", your are a visitor to SmartLib.</br>" .
//                    "Please make a regular account."
//            );
//
//            //Encode Answer
//            echo json_encode($result);
//            die();
//
//
//            //Show message to desktop user
//           //// echo   $_SESSION['topTypeMsg'].$_SESSION['topMsg'];
//           // die();
//
//        } //User is banned
//        else if ($reslevel == -2) {
//            $_SESSION['loggedin'] = "0";
//            //header("Location: userNotActivated.php");
//            $_SESSION['topTypeMsg'] = "err";
//            $_SESSION['topMsg'] = $_SESSION['name'] . ", your are banned from SmartLib.</br>" .
//                "Contact SmartLib Admin for further Details.";
//
//            $result = array(
//                "result" => "0",
//                "message" => $_SESSION['name'] . ", your are banned from SmartLib.</br>" .
//                    "Contact SmartLib Admin for further Details."
//        );
//
//            //Encode Answer
//            echo json_encode($result);
//            die();
//
//            //Show message to desktop user
//           // echo  $_SESSION['topTypeMsg']. $_SESSION['topMsg'];
//           // die();
//
//        } else {
//            $_SESSION['loggedin'] = "1";
//            unset($_SESSION['topTypeMsg']);
//            unset($_SESSION['topMsg']);
//            //User successfully logged in
//
//            //echo "1";
//            //Send Login Info to Desktop device
//            mobileSendLoginData($resusername, $resname, $ressurname,
//                $resemail, $restelephone, $resallowRequests, $reslevel);
//            die();
//
//        }
//
//        if ($_SESSION['currentPage'] == "") {
//            $_SESSION['currentPage'] = "../";
//        }
//
//        //header("Location: ".$_SESSION['currentPage']);
//
//
//    }

} //Users credencials are wrong
else {
//    if ($_SESSION['isMobileDevice']) {
    if (!$_SESSION['isMobileDevice']) {
        $_SESSION['loggedin'] = "0";
        }

        mobileSendLoginError();
//    } else {
//        // Inform the user about wrong username or password
//        $_SESSION['loggedin'] = "0";
//        //header("Location: userNotActivated.php");
//        $_SESSION['topTypeMsg'] = "err";
//        $_SESSION['topMsg'] = "Wrong Username or Password.</br>" .
//            "Please try again.";
//
//        if ($_SESSION['currentPage'] == "") {
//            $_SESSION['currentPage'] = "../";
//
//
//        }
//
//
//        $result = array(
//            "result" => "0",
//            "message" => "Wrong Username or Password.</br>" .
//                "Please try again."
//    );
//
//        //Encode Answer
//        echo json_encode($result);
//        die();
//
//
//        //TODO mod RM topMessage and check error!
//      //  echo  $_SESSION['topTypeMsg'].$_SESSION['topMsg'];
//      //  die();
//
//        //TODO mod
//        //header("Location: ".$_SESSION['currentPage']);
//    }


}

//////////////////// Functions

//
function dbError($pError)
{

    if ($_SESSION['isMobileDevice']) {
        //Inform Mobile Device about database Error
        mobileSendDatabaseError();
    }

    //if there is DB Error, inform user and move him back

    $_SESSION['topTypeMsg'] = "err";
    $_SESSION['topMsg'] = "Internal Error: " . $pError . "</br>You can't use SmartLib right now</br>" .
        "Please check back later.";


    if ($_SESSION['currentPage'] == "") {
        $_SESSION['currentPage'] = "../";
    }

    //TODO mod change inform in other file (included!)
    echo  $_SESSION['topTypeMsg'].$_SESSION['topMsg'];
    //header("Location: ".$_SESSION['currentPage']);
    die();

}


//Send login data to mobile device
function mobileSendLoginData($user, $name, $surname, $email, $telephone,
                             $allowRequests, $level)
{
    //Put Data in JSON Object
    //  $resemail; $resusername; $resname; $reslevel allowrequests

    $result = array(
        "result" => "1",
        "username" => "$user",
        "name" => "$name",
        "surname" => $surname,
        "email" => $email,
        "telephone" => $telephone,
        "allowRequests" => $allowRequests,
        "level" => $level
    );

    //Encode Answer
    echo json_encode($result);

    die();
}


// TODO Sends error to mobile device using JSON Object Format
function mobileSendDatabaseError()
{
    $result = array(
        "result" => "-11"
    );
    //Encode Answer
    echo json_encode($result);

    die();
}


// Sends error to mobile device using JSON Object Format
function mobileSendLoginError()
{
    $result = array(
        "result" => "0"
    );
    //Encode Answer
    echo json_encode($result);

    die();
}

//Sends login error to Website visitors
function webSendLoginError()
{

    $_SESSION['topTypeMsg'] = "err";
    $_SESSION['topMsg'] = "Username or Password Cant be empty";

    if ($_SESSION['currentPage'] == "") {
        $_SESSION['currentPage'] = "../";
    }


    echo   $_SESSION['topTypeMsg'].$_SESSION['topMsg'];
//	header("Location: ".$_SESSION['currentPage']);
    die();

}


?>
</body>
</html>






