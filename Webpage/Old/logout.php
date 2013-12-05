<!-- CHECK since this page is replaced, do we need to include headers and footers?? -->
<?php include 'header.php'; ?>

<?php


//CHECK why we start our session??
session_start();

//This function will destroy your session
session_destroy();

//CHECK needed???
//header("Location: http://www.cs.ucy.ac.cy/thesis/smartscan/index.php");
header("Location: index.php");
?>
<script type="text/javascript">
	//window.location.replace("http://www.cs.ucy.ac.cy/thesis/smartscan/index.php");
   window.location.replace("index.php");
   </script>
<?
exit();

//echo "You are now logged out! <a href=index.php>Index</a> or <a href=site_login.php>Login</a>";



?>

<?php include 'footer.php'; ?>