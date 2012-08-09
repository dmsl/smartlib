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


 // include 'sessionInit.php';

  

	$device="?device=web";
	$username="&username=".$_SESSION['username'];
	$mykey="&mykey=23f234trfv34";
	
	$buildURL= "http://".getCustom2ndURL()."mobile/getUserBooks.php".$device.$username.$mykey;
	
	
	$json = file_get_contents($buildURL,0,null,null);
	
	
	echo "Here are your Exported books:<br><br>";
	echo "In JSON Format:<br><br>";
	
	?>
	<textarea rows="15" cols="70">
<?php echo $json;?>
</textarea>

<?php 

echo "In XML Format:<br><br>";

$json_xml = json_to_xml($json);
	
?>
<textarea rows="15" cols="70">
<?php echo $json_xml;?>
</textarea>

<?php 

	
	// CAN USE ALSO http://jsontoxml.utilities-online.info/
	
	
	
	function getCustom2ndURL(){

	$len = strlen($_SERVER['REQUEST_URI']);
	$fullURL=$_SERVER['REQUEST_URI'];
	$found=0;


	for( $i= $len-1; $i>0; $i--){
		

		//Remove the last name of the URI
		if($fullURL[$i]=="/"){
				
			$found=1;
				
			$urlResult = substr($fullURL,0,$i);
			break;
		}

	}
	
	
	$len = strlen($urlResult);
	$fullURL=$urlResult;
	$found=0;


	for( $i= $len-1; $i>0; $i--){
		

		//Remove the last name of the URI
		if($fullURL[$i]=="/"){
				
			$found=1;
				
			$urlResult = substr($fullURL,0,$i+1);
			break;
		}

	}



	if(!$found)
		$urlResult = $_SERVER['REQUEST_URI'];

	return $_SERVER['SERVER_NAME']. $urlResult;

}
	
	
	?>