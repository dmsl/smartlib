<?php

include('../../dbConnect.php');

$isbn = $_GET["isbn"];
$title = $_GET["title"];
$username = $_GET["username"];

$term = $_GET["term"];
if (!$term) return;

$extra = "";

if ($username != "")
    $extra .= " AND username LIKE '%$username%'";
if ($title != "")
    $extra .= " AND title LIKE '%$title%' ";
if ($isbn != "")
    $extra .= " AND isbn LIKE '%$isbn%' ";

$sql = "select DISTINCT authors as authors " .
    " from SMARTLIB_USER U, SMARTLIB_BOOK_INFO BI, SMARTLIB_BOOK B " .
    " where U.U_ID=B.U_ID AND B.BI_ID=BI.BI_ID " .
    "AND authors LIKE '%$term%' $extra LIMIT 10";


$rsd = mysql_query($sql);

for ($x = 0, $numrows = mysql_num_rows($rsd); $x < $numrows; $x++) {
    $row = mysql_fetch_assoc($rsd);
    $authors[$x] = array("value" => $row["authors"]);
}
//return result
echo json_encode($authors);


?>