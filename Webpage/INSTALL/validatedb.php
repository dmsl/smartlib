<?php


$host = $_POST['host'];
$username = $_POST['username'];
$password = $_POST['password'];
$name = $_POST['name'];


// Connect to database
$dbconnect = mysql_pconnect($host, $username, $password) or printError(mysql_error());

//Select Database
mysql_select_db($name, $dbconnect) or printError(mysql_error());


$result = array(
    "result" => "1",
    "message" => "Database Credencials correct!"
);

echo json_encode($result);
die();


function printError($msg)
{

    $result = array(
        "result" => "0",
        "message" => "Error Connecting to database:<br>" . $msg
    );

    echo json_encode($result);
    die();

}

?>