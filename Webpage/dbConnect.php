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
    
    
    
require_once("CREDENCIALS.php");
include ('scripts/genericFunctions.php');

$loginError=0;



//Get the DB Connection from Session
$dbconnect=$_SESSION['dbconnection'];

 if(!mysql_ping($_SESSION['dbconnection'])) {
	connectToDB();
}



// ################ Functions ################

// ################ Connect to Database
function connectToDB(){
	/******
 * CHANGE those values below to your database host address,
* usernamee & password for database, and the database name.
*****/

	// Connect & Select database
	$dbconnect = mysql_pconnect(DB_HOST, DB_USER, DB_PASSWORD) or connectError(mysql_error());
	$_SESSION['dbconnection']=$dbconnect;//Save connection to Session

	if(!$loginError){
	//Select Database
	mysql_select_db(DB_NAME, $_SESSION['dbconnection']) or dbError(mysql_error());
	}
}
	
// ################ Disconnect from Database
function reconnectToDB(){
   mysql_close($_SESSION['dbconnection']);
   connectToDB();
}

function connectError($pError){
	$loginError=1;
	//$_SESSION['dbError']=1;
	inform($pError);
	}
	


?>