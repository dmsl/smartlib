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

	$_SESSION['REGusername']		= 
	$_SESSION['REGpassword']		= 
	$_SESSION['REGconfPassword']	=
	$_SESSION['REGname']			= 
	$_SESSION['REGsurname']			= 
	$_SESSION['REGemail']			= 
	$_SESSION['REGtelephone']		= 
	$_SESSION['REGappNotif']		=
	$_SESSION['REGemailNotif']		= 
	$_SESSION['regHasErrors'] =
	$_SESSION['regMessage'] = 
	$_SESSION['errUsername'] =
	$_SESSION['errPassword'] =
	$_SESSION['errConfPassword'] =
	$_SESSION['errMatchPassword'] =
	$_SESSION['errName'] =
	$_SESSION['errSurname'] =
	$_SESSION['errEmail'] =
	$_SESSION['errTelephone'] = ""
	;

	
	//Refresh the page
	header("Location: ../register.php"); 
	


?>
							

