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

//Save the users activation code
//	$useALink = $_REQUEST['uLnk'];
$_SESSION['owneruser'] = $_SESSION['username'];
$_SESSION['newadminuser'] = $_POST['newadminuser'];

//User activated using link
//	/if($useALink=="yes"){
//Save its username from the link

//Set a flag, so the activation will be legal
//		$_SESSION['LnkLegalActivation']="Y";
//		}


//
//	if(strlen($usersActivationCode)!=10){
////		$_SESSION['actMessage'] = "Wrong Activation Code.";
//
//        $result = array(
//            "result"=>"0",
//            "message"=>"Wrong Activation Code"
//        );
//
//        echo json_encode($result);
//        die();
//
////		header("Location: ../registerSuccess.php");
//		}
//	else {
//Connect to database
include('../dbConnect.php');

//Check if Users Act.Code and database Act.Code Match

makeUserAdmin();


//		}


// Functions

//Returns the activation Code from Database
function makeUserAdmin()
{
    $actCodeQRes =
        mysql_query("SELECT level FROM SMARTLIB_USER WHERE username='" . $_SESSION['owneruser'] . "'");
    $actCodeQRes = mysql_fetch_array($actCodeQRes);
    $level = $actCodeQRes['level'];


    ///Account already activated
    if ($level != "3") {

        $result = array(
            "result" => "0",
            "message" => "You are not allowed to make an Admin."
        );

        echo json_encode($result);
        die();


    } else {
        //TODO
        makeAdmin();
    }

}

//Activates the user
function makeAdmin()
{

    //Get new admin U_ID
    $actCodeQRes =
        mysql_query("SELECT U_ID FROM SMARTLIB_USER WHERE username='" . $_SESSION['newadminuser'] . "'");
    $actCodeQRes = mysql_fetch_array($actCodeQRes);
    $newadmin_U_ID = $actCodeQRes['U_ID'];


    $activationresult =
        mysql_query("UPDATE SMARTLIB_USER SET level='2' WHERE U_ID='" . $newadmin_U_ID . "'");

//		echo "Activation Result: $activationresult";

    if ($activationresult == "1") {
        //Unset Error Info Message
//			unset($_SESSION['actMessage']);
//			header("Location: ../registerActivated.php");
        $result = array(
            "result" => "1",
            "message" => "User successfully upgraded to amdin"
        );

        echo json_encode($result);
        die();
    } else {
        //Confirmations Matched but failed to activate User --> Database Error
//			$_SESSION['actMessage'] = "Failed to activate user.</br>Database Internal Eror";
//			header("Location: ../registerSuccess.php");
        $result = array(
            "result" => "0",
            "message" => "Failed to make user admin :("
        );

        echo json_encode($result);
        die();

    }

}

?>