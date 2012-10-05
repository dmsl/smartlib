<?php

include('../../dbConnect.php');

$isbn = $_GET["isbn"];
$authors = $_GET["authors"];
$title = $_GET["title"];

$term = $_GET["term"];
if (!$term) return;

$extra = "";

if ($title != "")
    $extra .= " AND title LIKE '%$title%'";
if ($authors != "")
    $extra .= " AND authors LIKE '%$authors%' ";
if ($isbn != "")
    $extra .= " AND isbn LIKE '%$isbn%' ";

$sql = "select DISTINCT username as username " .
    " from SMARTLIB_USER U, SMARTLIB_BOOK_INFO BI, SMARTLIB_BOOK B " .
    " where U.U_ID=B.U_ID AND B.BI_ID=BI.BI_ID " .
    "AND username LIKE '%$term%' $extra  LIMIT 10";


$rsd = mysql_query($sql);

for ($x = 0, $numrows = mysql_num_rows($rsd); $x < $numrows; $x++) {
    $row = mysql_fetch_assoc($rsd);
    $usernames[$x] = array("value" => $row["username"]);
}
//return result
echo json_encode($usernames);


?>