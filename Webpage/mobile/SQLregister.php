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
require_once("../CONFIG.php");
include_once("scripts/genericFunctions.php");


$_SESSION['isMobileDevice'] = 0;
$_SESSION['foundLevel'] = 0;

//Get the device
$device = $_POST['device'];

//Get username and password from our form (login.php)
$username = $_POST['username'];
$password = $_POST['password'];

//Find out if we are on mobile device
if ($device == "android" || $device == "iOS") {
    $_SESSION['isMobileDevice'] = 1;
}


//Get data from Website or Smartphones
$_SESSION['REGusername'] = $_POST['username'];
$_SESSION['REGpassword'] = $_POST['password'];
$_SESSION['REGconfPassword'] = $_POST['confPassword'];
$_SESSION['REGname'] = $_POST['name'];
$_SESSION['REGsurname'] = $_POST['surname'];
$_SESSION['REGemail'] = $_POST['email'];
$_SESSION['REGtelephone'] = $_POST['telephone'];
$_SESSION['REGappNotif'] = $_POST['appNotif'];
$_SESSION['REGemailNotif'] = $_POST['emailNotif'];

//Workaround
if ($_SESSION['REGappNotif'] == "true")
    $_SESSION['REGappNotif'] = "on";
else
    $_SESSION['REGappNotif'] = "";

if ($_SESSION['REGemailNotif'] == "true")
    $_SESSION['REGemailNotif'] = "on";
else
    $_SESSION['REGemailNotif'] = "";


//Save errors to inform user
$_SESSION['regHasErrors'] = "0";
$_SESSION['regMessage'] = "";


//Gather errors
if ($_SESSION['REGusername'] == "") {
    $_SESSION['errUsername'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Username can't be empty</br>";
} else if (strlen($_SESSION['REGusername']) > 15) {
    $_SESSION['errUsername'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Username can't exceed 15 characters</br>";
} else
    $_SESSION['errUsername'] = "";

if ($_SESSION['REGpassword'] == "") {
    $_SESSION['errPassword'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Password can't be empty</br>";
} else
    $_SESSION['errPassword'] = "";

if ($_SESSION['REGconfPassword'] == "") {
    $_SESSION['errConfPassword'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Password Confirmation can't be empty</br>";
} else
    $_SESSION['errConfPassword'] = "";

if ($_SESSION['REGpassword'] != $_SESSION['REGconfPassword']) {
    $_SESSION['errMatchPassword'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Password and Confirmation dont match</br>";
} else
    $_SESSION['errMatchPassword'] = "";

if ($_SESSION['REGname'] == "") {
    $_SESSION['errName'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Name cant be empty</br>";
} else
    $_SESSION['errName'] = "";

if ($_SESSION['REGsurname'] == "") {
    $_SESSION['errSurname'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Surname cant be empty</br>";
} else
    $_SESSION['errSurname'] = "";

if ($_SESSION['REGemail'] == "") {
    $_SESSION['errEmail'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Email cant be empty</br>";
} else if (!isEmailCorrect($_SESSION['REGemail'])) {
    $_SESSION['errEmail'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Email Address is Wrong</br>";
} else
    $_SESSION['errEmail'] = "";

if ($_SESSION['REGtelephone'] == "") {
    $_SESSION['errTelephone'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Telephone cant be empty</br>";
} else
    $_SESSION['errTelephone'] = "";


//Connect to database
include('../dbConnect.php');

//Check if is the first User (= Admin/Owner)

$queryFirstUser = sprintf("SELECT username FROM SMARTLIB_USER");
$allUsernames = mysql_query($queryFirstUser);
$allUsernamesNum = mysql_num_rows($allUsernames);


//User level is admin
if ($allUsernamesNum == 0) {
    $_SESSION['foundLevel'] = "3";
} else {
    $_SESSION['foundLevel'] = "0";
}

// Check username uniqueness
$queryFindUsernames = sprintf("SELECT username FROM SMARTLIB_USER WHERE username='%s'",
    mysql_real_escape_string($_SESSION['REGusername']));


$usernameMatches = mysql_query($queryFindUsernames);
$usernameMatchesNum = mysql_num_rows($usernameMatches);

if ($usernameMatchesNum > 0) {
    $_SESSION['errUsername'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Username already registered</br>";
}


// Check email uniqueness
$emailMatches = mysql_query("SELECT email FROM SMARTLIB_USER WHERE email='" . $_SESSION['REGemail'] . "'");
$emailMatchesNum = mysql_num_rows($emailMatches);

//Already registered email
if ($emailMatchesNum > 0) {
    $_SESSION['errEmail'] = "1";
    $_SESSION['regHasErrors'] = 1;
    $_SESSION['regMessage'] .= "Email already registered</br>";
}


//Registration Input was correct
if ($_SESSION['regHasErrors'] == "0") {

    //Register user to database
    registerUserToDatabase();

    //Registration completed
    if ($_SESSION['regHasErrors'] == "0") {
        if ($_SESSION['isMobileDevice']) {
            mobileSendLoginSuccess();
        } else {

//            printError(); TODO make it ajax! with reply

            $msg = "";

            if ($_SESSION['foundLevel'] == "3") {
                $msg = "Your administrator/owner account successfully created<br>" .
                    " No activation threw email needed for this account.<br>" .
                    "All other accounts must be activated using their email address given.";
            } else {
                $msg = "Your account successfully created<br>" .
                    "Please Activate it using your email: <br>" . $_SESSION['REGemail'];

            }


            $result = array(
                "result" => "1",
                "message" => $msg
            );

            echo json_encode($result);
            die();
        }
        //Show user info and activation details with email!
//            header("Location: ../registerSuccess.php");
//        }

    }

}

//Registration Info is wrong
if ($_SESSION['regHasErrors'] != "0") {

    if ($_SESSION['isMobileDevice']) {
        mobileSendLoginError();
    } else {
        printError();
//        header("Location: ../register.php");
    }

}

function printError()
{
    $result = array(
        "result" => "0",
        "message" => $_SESSION['regMessage']
    );
    //Hide other info

    $_SESSION['regHasErrors'] = 0;

    echo json_encode($result);
    die();

}


//Functions
//Generates the Activation Code
function generateActivationCode()
{
    $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    $str = "";

    $size = strlen($chars);
    for ($i = 0; $i < 10; $i++) {
        $str .= $chars[rand(0, $size - 1)];
    }

    return $str;
}

//Registers a user to Database
function registerUserToDatabase()
{

    //Generate User's Activation Code
    $activationCode = generateActivationCode();

    //Increase protection
    $salt = _SALT;
    $pepper = _PEPPER;

    $encPassword = $_SESSION['REGpassword'];
    // Put salt&pepper on password
    $encPassword = $salt . $encPassword . $pepper;

    // Password Encryption
    $encPassword = md5($encPassword);

    $allowRequests = 0;


    //User allow app Notifications
    if ($_SESSION['REGappNotif'] == "on") {
        //User allows Both Notifications
        if ($_SESSION['REGemailNotif'] == "on") {
            $allowRequests = 3;
        } //User wants only App notifications
        else $allowRequests = 1;
    } //User wants only Email notifications
    else if ($_SESSION['REGemailNotif'] == "on") {
        $allowRequests = 2;
    } //User wont share its books
    else $allowRequests = 0;

    $queryInsertUser = sprintf("INSERT INTO SMARTLIB_USER (username, password, name, surname, email,"
            . "telephone,allowRequests,activationCode, level) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
        mysql_real_escape_string($_SESSION['REGusername']),
        mysql_real_escape_string($encPassword),
        mysql_real_escape_string($_SESSION['REGname']),
        mysql_real_escape_string($_SESSION['REGsurname']),
        mysql_real_escape_string($_SESSION['REGemail']),
        mysql_real_escape_string($_SESSION['REGtelephone']),
        $allowRequests,
        mysql_real_escape_string($activationCode),
        $_SESSION['foundLevel']
    );

    //Insert User to database
    $insert = mysql_query($queryInsertUser) or dbError(mysql_error());


    //Library has a email server
    if ($_SESSION['foundLevel'] == "3") {

        $strTo = $_SESSION['REGemail'];
        $strSubject = "SmartLib " . "UCY" . " Activation";
        $strHeader = "From: Smartlib UCY<" . _EMAIL . ">";
        $strMessage = "Hello " . $_SESSION['REGname'] . ",\nWelcome to the library of the modern world.\n" .
            "\n\nTo activate your account please follow this link: \n\n" .
            getCustom2ndURL() .
            "activate.php?uLnk=yes&uLnkUsername=" . $_SESSION['REGusername'] .
            "&activationCode=" . $activationCode . "\n\nThank you,\nSmartLib Team";


        // @ = avoid showing error
        //$flgSend = ;

        if (@mail($strTo, $strSubject, $strMessage, $strHeader)) {
        } else {
            $_SESSION['errEmail'] = "1";
            $_SESSION['regHasErrors'] = "1";
            $_SESSION['regMessage'] .= "Email address is invalid!</br>";

            printError();

        }
    } else {

        $strTo = $_SESSION['REGemail'];
        //TODO change to the Orginization Name
        $strSubject = "SmartLib " . "UCY" . " Activation";
        $strHeader = "From: Smartlib UCY<" . _EMAIL . ">";
        $strMessage = "Hello " . $_SESSION['REGname'] . ",\nWelcome to the library of the modern world.\n" .
            "\n\nTo activate your account please follow this link: \n\n" .
            getCustom2ndURL() .
            "activate.php?uLnk=yes&uLnkUsername=" . $_SESSION['REGusername'] .
            "&activationCode=" . $activationCode . "\n\nThank you,\nSmartLib Team";


        // @ = avoid showing error
        //$flgSend = ;

        if (@mail($strTo, $strSubject, $strMessage, $strHeader)) {
        } else {
            $_SESSION['errEmail'] = "1";
            $_SESSION['regHasErrors'] = "1";
            $_SESSION['regMessage'] .= "Email address is invalid!</br>";

            printError();

        }

    }


}

////////////// Functions

//Returns the URL user is, without include the last page in the URL path

function getCustomURL()
{

    $len = strlen($_SERVER['REQUEST_URI']);

    for ($i = $len - 1; $i > 0; $i--) {
        //Remove the last name of the URI
        if ($_SERVER['REQUEST_URI'][$i] == "/") {

            $found = 1;

            $urlResult = substr($_SERVER['REQUEST_URI'], 0, $i + 1);
            break;
        }

    }

    if (!$found)
        $urlResult = $_SERVER['REQUEST_URI'];

    return $_SERVER['SERVER_NAME'] . $urlResult;

}


//Checks if the email is correct
function isEmailCorrect($email)
{
    // First, we check that there's one @ symbol,
    // and that the lengths are right.
    if (!ereg("^[^@]{1,64}@[^@]{1,255}$", $email)) {
        // Email invalid because wrong number of characters
        // in one section or wrong number of @ symbols.
        return false;
    }
    // Split it into sections to make life easier
    $email_array = explode("@", $email);
    $local_array = explode(".", $email_array[0]);
    for ($i = 0; $i < sizeof($local_array); $i++) {
        if
        (!ereg("^(([A-Za-z0-9!#$%&'*+/=?^_`{|}~-][A-Za-z0-9!#$%&
				↪'*+/=?^_`{|}~\.-]{0,63})|(\"[^(\\|\")]{0,62}\"))$",
            $local_array[$i])
        ) {
            return false;
        }
    }
    // Check if domain is IP. If not,
    // it should be valid domain name
    if (!ereg("^\[?[0-9\.]+\]?$", $email_array[1])) {
        $domain_array = explode(".", $email_array[1]);
        if (sizeof($domain_array) < 2) {
            return false; // Not enough parts to domain
        }
        for ($i = 0; $i < sizeof($domain_array); $i++) {
            if
            (!ereg("^(([A-Za-z0-9][A-Za-z0-9-]{0,61}[A-Za-z0-9])|
					↪([A-Za-z0-9]+))$",
                $domain_array[$i])
            ) {
                return false;
            }
        }
    }
    return true;

}

//Mobile Device Functions
// Sends error to mobile device using JSON Object Format
function mobileSendLoginError()
{
    //Convert HTML New Line to Java New Line
    $javaMSG = $_SESSION['regMessage'];
    $oldNL = "</br>";
    $newNL = "\n";

    $offset = 0;
    $i = 1;
    $tmpOldStrLength = strlen($oldNL);
    while (($offset = strpos($javaMSG, $oldNL, $offset)) !== false) {
        $javaMSG = substr_replace($javaMSG, $newNL, $offset, $tmpOldStrLength);
    }


    $result = array(
        "result" => "0",
        "message" => $javaMSG
    );
    //Encode Answer
    echo json_encode($result);

    die();
}

//Mobile Device Functions
// Sends error to mobile device using JSON Object Format
function mobileSendLoginSuccess()
{
    $result = array(
        "result" => "1"
    );
    //Encode Answer
    echo json_encode($result);

    die();
}


// TODO Sends error to mobile device using JSON Object Format
function mobileSendDatabaseError()
{
    $result[] = array(
        "result" => "-11"
    );
    //Encode Answer
    echo json_encode($result);

    die();
}


// Database Error
function dbError($pError)
{

    if ($_SESSION['isMobileDevice']) {
        //Inform Mobile Device about database Error
        mobileSendDatabaseError();
    }

    //if there is DB Error, inform user and move him back
    //inform($pError);

    $result = array(
        "result" => "0",
        "message" => "Database Error :("
    );
    //Hide other info

    $_SESSION['regHasErrors'] = 0;
    echo json_encode($result);
    die();


    //header("Location: ../index.php");
//    die();

}


?>