<!-- 
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
    
    
    -->
    <?php 
//Get the device, ISBN & Username
$name = $_POST['name'];
$url = $_POST['url'];
$email = $_POST['email'];
$telephone = $_POST['telephone'];
$town = $_POST['town'];
$country = $_POST['country'];

//Make '+' URL Friendly
$telephone  = str_replace("+", "%2B", $telephone);


$enroller = 'http://www.cs.ucy.ac.cy/projects/smartLib/MASTER/enroll.php';
$myvars = 'name=' . $name . '&url=' . $url
. '&email=' . $email  . '&town=' . $town  . '&country=' . $country
. '&telephone=' . $telephone;



$ch = curl_init( $enroller );
curl_setopt( $ch, CURLOPT_POST, 1);
curl_setopt( $ch, CURLOPT_POSTFIELDS, $myvars);
curl_setopt( $ch, CURLOPT_FOLLOWLOCATION, 1);
curl_setopt( $ch, CURLOPT_HEADER, 0);
curl_setopt( $ch, CURLOPT_RETURNTRANSFER, 1);

$response = curl_exec( $ch );




echo $response."<br><br>If registration succeed, proceed to create database tables:<br><br>";

?>
<FORM METHOD="LINK" ACTION="initLibrary2.php">
<INPUT TYPE="submit" VALUE="Create Tables">

</FORM>