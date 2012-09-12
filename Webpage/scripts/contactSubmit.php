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
require_once("../CREDENCIALS.php");

	$_SESSION['CONTmessage'] = $_REQUEST['CONTmessage'];
	$_SESSION['CONTname'] = $_REQUEST['CONTname'];
	$_SESSION['CONTemail'] = $_REQUEST['CONTemail'];
	
	//User have to fill all data
	if($_SESSION['CONTmessage']=="" || $_SESSION['CONTname'] == "" || $_SESSION['CONTemail']==""){
		//Show error to user	
		$_SESSION['topTypeMsg']="err";
		$_SESSION['topMsg']="Please fill all form fields.";

		//Move user to current page(which is registration form)
		header("Location: ../contact.php"); 
		die(); 
		}
	
	//User gave a wrong email
	if(!isEmailCorrect($_SESSION['CONTemail'])){
		//Show error to user	
		$_SESSION['topTypeMsg']="err";
		$_SESSION['topMsg']="The email address you entered is wrong.";

		//Move user to current page(which is registration form)
		header("Location: ../contact.php");
		die(); 
		}
	

// SMARTLIB ADMIN MAIL
$strTo =  _EMAIL;

//Build Email
$strSubject = "SmartLib " . _NAME . " Contact Form";  
$strHeader = "From: Smartlib "._NAME." Contact<"._EMAIL.">";  
$strMessage = "From User: ". $_SESSION['CONTname'] ."\nWith Email: ".$_SESSION['CONTemail'].
			"\n\nMessage:\n". $_SESSION['CONTmessage'] .
			"\n\nSmartLib "._NAME." Contact Form"; 

//Send email
// @ = avoid showing error
$flgSend = @mail($strTo,$strSubject,$strMessage,$strHeader);

	if(!$flgSend)  
	{
	//Show error to user	
	$_SESSION['topTypeMsg']="err";
	$_SESSION['topMsg']="Internal Error: Mail System Failed.</br>Please try again Later";
	}
	else{
	//Show info to user	
	$_SESSION['topTypeMsg']="info";
	$_SESSION['topMsg']="You message was successfully send to SmartLib. Thank you.";
		
		//Reset all messages from Session
	$_SESSION['CONTmessage']		= 
	$_SESSION['CONTname']		= 
$_SESSION['CONTemail'] = "";
		}
		
	//Load home page
	header("Location: ../index.php");
	
	
	
	
///////////////////////////////// Functions

//Checks if the email is correct

//Checks if the email is correct
function isEmailCorrect($email){
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
$local_array[$i])) {
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
$domain_array[$i])) {
        return false;
      }
    }
  }
  return true;
  
}

?>