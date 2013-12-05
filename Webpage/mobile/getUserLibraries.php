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
	$sql = sprintf("select id, name, url, email, telephone, town, country from SMARTLIB_LIBRARY where activated > 0 and u_id= %d order by name", $uid);
	$libs = mysql_query($sql);
	if (!$libs) {
		echo $fail;
		exit;
	}
	$result[] = array("result" => "1");
	while ($row = mysql_fetch_assoc($libs)) {
		$row["createdby"] = $username;
		$result[] = $row;
	}
	echo json_encode($result);
?>