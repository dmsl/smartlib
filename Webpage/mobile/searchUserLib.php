<?php 
	$device = isset($_POST['device']) ? strtolower(trim($_POST['device'])) : NULL;
	$username = isset($_POST['username']) ? strtolower(trim($_POST['username'])) : NULL;
	$keyword = isset($_POST['keyword']) ? strtolower(trim($_POST['keyword'])) : "";
	$uid = 0;
	
	function do_exit($code = 0)
	{
		$str_code = $code . "";
		$res[] = array("result" => $str_code);
		echo json_encode($res);
		exit;
	}
	
	function is_valid($device, $username, $search)
	{
		$valid = false;
		
		if (isset($device) && isset($username))
		{
			if ($device == "android" || $device == "ios")
			{
				$valid = true;
			}
		}

		return $valid;
	}
	
	if (!is_valid($device, $username, $search))
	{
		do_exit(-12);
	}
	
	include("../dbConnect.php");
	
	$sql = sprintf("SELECT 
			U_ID 
		FROM SMARTLIB_USER 
		WHERE username = '%s'", 
		mysql_real_escape_string($username));
	
	$res = mysql_query($sql);
	
	if (mysql_num_rows($res) == 0)
	{
		do_exit(-11);
	}
	
	$uid = mysql_result($res, 0);
	
	$sql = sprintf("SELECT 
			L.id, u.username as createdby, 
			L.name, 
			L.url, 
			L.email, 
			L.telephone, 
			L.town, 
			L.country 
		FROM SMARTLIB_LIBRARY L 
			LEFT JOIN SMARTLIB_USER u ON L.u_id = u.u_id 
		WHERE activated='1' 
		AND L.u_id = %d 
		AND LOWER(L.name) LIKE '%s%s%s' 
		ORDER BY L.name",
		$uid, 
		"%", $keyword, "%");
		
	$res = mysql_query($sql);
	$count = mysql_num_rows($res);
	
	if ($count == 0)
	{
		do_exit();
	}
	
	$libs[] = array("result" => "1", "libsNum" => $count . "");
	
	while ($row = mysql_fetch_assoc($res))
	{
		$libs[] = $row;
	}
	
	echo json_encode($libs);
?>