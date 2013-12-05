<?php 
	session_start();
	$device = isset($_POST['device']) ? strtolower(trim($_POST['device'])) : null;
	$username = isset($_POST['username']) ? strtolower(trim($_POST['username'])) : null;
	$uid = 0;
	$fail = json_encode(array("result" => "0"));
	if ($device != "android" && $device != "ios") {
		echo $fail;
		exit;
	}
	include('../dbConnect.php');
	$params = array(
		"name" => $_POST["name"],
		"url" => $_POST["url"],
		"email" => $_POST["email"],
		"tel" => isset($_POST["tel"]) ? $_POST["tel"] : "",
		"town" => isset($_POST["town"]) ? $_POST["town"] : "Nicosia",
		"country" => isset($_POST["country"]) ? $_POST["country"] : "Cyprus"
	);
	foreach ($params as $key => $value) { 
		if (!isset($value)) { // validate
			echo $fail;
			exit;
		}
		$params[$key] = mysql_real_escape_string($value); // escape
	}
	if ($username != null) {
		$username = mysql_real_escape_string($username);
		$sql = sprintf("select u_id, username from SMARTLIB_USER where lower(username) = '%s' LIMIT 1", $username);
		$user = mysql_query($sql);
		if ($user) {
			$uid = mysql_fetch_object($user)->u_id;
		}
	}
	if ($uid == 0) {
		echo $fail;
		exit;
	}
	$sql = sprintf("insert into SMARTLIB_LIBRARY(U_ID, NAME, URL, EMAIL, TELEPHONE, TOWN, COUNTRY, ACTIVATED, ACTIVATIONDATE) values (%d, '%s', '%s', '%s', '%s', '%s', '%s', 1, NOW())", 
			$uid,
			$params["name"],
			$params["url"],
			$params["email"],
			$params["tel"],
			$params["town"],
			$params["country"]);
	if (!mysql_query($sql)) {
		echo $fail;
		exit;
	}
	$id = mysql_insert_id();
	echo json_encode(array("result" => "1", "id" => $id));
?>