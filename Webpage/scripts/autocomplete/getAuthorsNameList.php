<?php
require_once "config.php";

$q = strtolower($_GET["q"]);
if (!$q) return;

$sql = "select DISTINCT authors as authors from SMARTLIB_BOOK_INFO where authors LIKE '%$q%'";

$rsd = mysql_query($sql);
while ($rs = mysql_fetch_array($rsd)) {
    $cname = $rs['authors'];
    echo "$cname\n";
}
?>