<?php
/**
 * Created by JetBrains PhpStorm.
 * User: paschalis
 * Date: 9/30/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */


//Get data


$dbname = $_POST['dbname'];
$dbhost = $_POST['dbhost'];
$dbuser = $_POST['dbuser'];
$dbpass = $_POST['dbpass'];
$libname = $_POST['libname'];
$libshortname = $_POST['libshortname'];
$orgname = $_POST['orgname'];
$addr1 = $_POST['addr1'];
$addr2 = $_POST['addr2'];
$addr3 = $_POST['addr3'];
$town = $_POST['town'];
$country = $_POST['country'];
$telephone = $_POST['telephone'];
$fax = $_POST['fax'];
$email = $_POST['email'];
$depturl = $_POST['depturl'];
$orgurl = $_POST['orgurl'];
$key = $_POST['key'];
$pepper = $_POST['pepper'];
$salt = $_POST['salt'];
//TODO CHANGE THIS TO ../CONFIG!
$filename = "../CONFIG.php";


//
$fw = fopen($filename, 'w') or showError('Dont have permissions on /CONFIG.php to save data<br>'
    . 'Please change permissions temporarily to 0777 and try again');


$data = <<< ALLDATA
 <?php

////////// AUTO GENERATED CONFIGURATION FILE ////////////

// The name of the database
define('DB_NAME', '$dbname');
//Host of the database
define('DB_HOST', '$dbhost');
//Dont change this - Automatically generated
define('DB_DSN','mysql:host='.DB_HOST.';dbname='.DB_NAME);
//DB Username
define('DB_USER', '$dbuser');
// Db password
define('DB_PASSWORD', '$dbpass');

//Full name of library
define('_FULL_NAME', '$libname');
//Organization full name
define('_ORG_FULL_NAME', '$orgname');
//Short Name
define('_NAME', '$libshortname');
//Address of library! (Seperated with break)
define('_ADDRESS',
'$addr1<br>
$addr2<br>
$addr3<br>
$town, $country<br>'
);
//Full telephone
define('_TELEPHONE', '$telephone');
//Fax - Optional
define('_FAX', '$fax');
//Email
define('_EMAIL', '$email');
//Department URL
define('_DEPARTMENT_URL', '$depturl');
//Organization URL
define('_ORGANIZATION_URL', '$orgurl');
//Key defined for safety in some webpages
define('_MY_KEY', '$key');

//Salt for database password protection
define('_SALT', '$salt');

//Salt for database password protection
define('_PEPPER', '$pepper');

?>

ALLDATA;


$fb = fwrite($fw, $data) or showError('Could not write
to file Config.php. <br>'
    . 'Please change permissions temporarily to 0777 and try again');

// close file
fclose($fw);

$result = array(
    "result" => "1",
    "message" => "Configuration Files Saved Successfully"
);

echo json_encode($result);
die;


function showError($msg)
{

    $result = array(
        "result" => "0",
        "message" => $msg
    );

    echo json_encode($result);
    die;

}


include('../dbConnect.php');

// Create new tables

$fp = file('CREATE_DATABASE.sql', FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
$query = '';
foreach ($fp as $line) {
    if ($line != '' && strpos($line, '--') === false) {
        $query .= $line;
        if (substr($query, -1) == ';') {
            mysql_query($query) or showError("Error while initializing database:<br>" . mysql_error());
            $query = '';
        }
    }
}







?>