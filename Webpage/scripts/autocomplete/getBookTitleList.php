<?php
require_once "config.php";

$q = strtolower($_GET["q"]);
if (!$q) return;

$sql = "select DISTINCT title as title from SMARTLIB_BOOK_INFO where title LIKE '%$q%'";

$rsd = mysql_query($sql);
while ($rs = mysql_fetch_array($rsd)) {
    $cname = $rs['title'];
    echo "$cname\n";
}
?>