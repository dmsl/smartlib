<?php
require_once("../../CREDENCIALS.php");
$host_name = DB_HOST;
$user_name = DB_USER;
$pass_word = DB_PASSWORD;
$database_name = DB_NAME;

$conn = mysql_connect($host_name, $user_name, $pass_word) or die ('Error connecting to dATABASE');
mysql_select_db($database_name);

?>