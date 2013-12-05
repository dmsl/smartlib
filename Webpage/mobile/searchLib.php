<?php 
	$device = isset($_POST['device']) ? strtolower(trim($_POST['device'])) : NULL;
	$keyword = isset($_POST['keyword']) ? strtolower(trim($_POST['keyword'])) : "";
	$uid = 0;
	
	function do_exit($code = 0)
	{
		$str_code = $code . "";
		$res[] = array("result" => $str_code);
		echo json_encode($res);
		exit;
	}
	
	function is_valid($device, $search)
	{
		$valid = false;
		
		if (isset($device))
		{
			if ($device == "android" || $device == "ios")
			{
				$valid = true;
			}
		}

		return $valid;
	}
	
	if (!is_valid($device, $search))
	{
		do_exit(-12);
	}
	
	include("../dbConnect.php");
	
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
		AND LOWER(L.name) LIKE '%s%s%s' 
		ORDER BY L.name",
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