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
	$useALink = $_REQUEST['uLnk'];
	$usersActivationCode = $_REQUEST['activationCode'];
	
	//User activated using link
	if($useALink=="yes"){
		//Save its username from the link
		$_SESSION['REGusername'] = $_REQUEST['uLnkUsername'];
		//Set a flag, so the activation will be legal
		$_SESSION['LnkLegalActivation']="Y";
		}
		
		

	
	
	if(strlen($usersActivationCode)!=10){
		$_SESSION['actMessage'] = "Wrong Activation Code.";
		header("Location: ../registerSuccess.php");
		}
	else {
		//Connect to database
		include('../dbConnect.php'); 
		
		//Check if Users Act.Code and database Act.Code Match
		if(getDBActivationCode() == $usersActivationCode){
			echo "activating user";
			activateUser();
			}
		else{
		$_SESSION['actMessage'] = "Wrong Activation Code.";
		header("Location: ../registerSuccess.php");
			}
		

		
		}
		
		
// Functions

//Returns the activation Code from Database
function getDBActivationCode(){
		$actCodeQRes =
		mysql_query("SELECT activationCode,U_ID,name FROM SMARTLIB_USER WHERE username='".$_SESSION['REGusername']."'");
		$actCodeQRes = mysql_fetch_array($actCodeQRes);
		$_SESSION['userID'] = $actCodeQRes['U_ID'];
		$_SESSION['REGname'] = $actCodeQRes['name'];
		return $actCodeQRes['activationCode'];
	}
	
//Activates the user
function activateUser(){
		$activationresult =
		mysql_query("UPDATE SMARTLIB_USER SET level='1',activationCode=null WHERE U_ID='".$_SESSION['userID']."'");
		
		echo "Activation Result: $activationresult";
		if($activationresult=="1"){
			//Unset Error Info Message
			unset($_SESSION['actMessage']);
			header("Location: ../registerActivated.php");
			}
		else {
			//Confirmations Matched but failed to activate User --> Database Error
			$_SESSION['actMessage'] = "Failed to activate user.</br>Database Internal Eror";
			header("Location: ../registerSuccess.php");
			}

	}
			
?>