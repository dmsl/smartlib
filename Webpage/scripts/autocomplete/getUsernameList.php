<?php
require_once "config.php";

$title = $_GET["title"];
$authors = $_GET["authors"];
$isbn = $_GET["isbn"];

$q = strtolower($_GET["q"]);
if (!$q) return;

$extra = "";

if ($title != "")
    $extra .= " AND title LIKE '%$title%' ";
if ($authors != "")
    $extra .= " AND authors LIKE '%authors%'";
if ($isbn != "")
    $extra .= " AND isbn LIKE '%$isbn%' ";

$sql = "select DISTINCT username as username from SMARTLIB_USER where username LIKE '%$q%' $extra";

$rsd = mysql_query($sql);
while ($rs = mysql_fetch_array($rsd)) {
    $cname = $rs['username'];
    echo "$cname\n";
}
?>