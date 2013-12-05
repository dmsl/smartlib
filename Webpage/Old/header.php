<?php
// TODO check. where is this function?
session_start();
?>

<!DOCTYPE html>
<html lang="en">

<head>
<!-- WebPage Title 
Here the organization goes
-->
<title>SmartLib __ORG__</title>
<meta charset="utf-8">
<!-- 	CHECK!
what below lines do?
-->
<link rel="stylesheet" href="./css/reset.css" type="text/css"
	media="all">
<link rel="stylesheet" href="./css/layout.css" type="text/css"
	media="all">
<link rel="stylesheet" href="./css/style.css" type="text/css"
	media="all">
<script type="text/javascript" src="./js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="./js/cufon-yui.js"></script>
<script type="text/javascript" src="./js/cufon-replace.js"></script>
<script type="text/javascript" src="./js/Myriad_Pro_400.font.js"></script>
<script type="text/javascript" src="./js/Myriad_Pro_700.font.js"></script>
<script type="text/javascript" src="./js/Myriad_Pro_600.font.js"></script>
<!--[if lt IE 9]>
	<script type="text/javascript" src="http://info.template-help.com/files/ie6_warning/ie6_script_other.js"></script>
	<script type="text/javascript" src="./js/html5.js"></script>
<![endif]-->


<!-- 	CHECK!
Show the jCarousel
-->
<link href="./css/jquery.jcarousel.css" rel="stylesheet" type="text/css"
	media="screen">

<link href="./css/jquery.jcarousel.skin.css" rel="stylesheet"
	type="text/css" media="screen">

<script type="text/javascript" src="./js/jquery-1.2.6.min.js"></script>

<script type="text/javascript" src="./js/jquery.selectbox.js"></script>

<script type="text/javascript" src="./js/jquery.dimensions.js"></script>

<script type="text/javascript" src="./js/jquery.jcarousel.pack.js"></script>

<script type="text/javascript" src="./js/jquery.reflect.js"></script>

<script type="text/javascript" src="./js/ie6_pngfix.js"></script>

<script type="text/javascript" src="./js/frontpage.js"></script>

<script type="text/javascript" src="./js/grid.locale-en.js"></script>

<script type="text/javascript" src="./js/jquery-ui-1.8.16.custom.min.js"></script>

<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>

<link href="./css/ui.jqgrid.css" rel="stylesheet" type="text/css"
	media="screen">

<link href="./css/jquery-ui-1.8.18.custom.css" rel="stylesheet"
	type="text/css" media="screen">

<link rel="stylesheet" type="text/css" href="css/tipsy.css" />

<script type="application/javascript" src="js/jquery.tipsy.js"></script>

<link rel="stylesheet" type="text/css" href="stars/rating.css" />

<script type="application/javascript" src="stars/jquery.rating.js"></script>

<script type="text/javascript">

	$.jgrid.no_legacy_api = true;

	$.jgrid.useJSON = true;
	

</script>

<style>
.ui-jqgrid tr.jqgrow td {
	vertical-align: middle
}
</style>

</head>

<?php
// TODO  find what this do
$username = $_SESSION['username'];
$password = $_SESSION['password'];
?>
<!-- CHECK before that was a list, with menu(and if login, mybooks was appearing!) -->
<!--<div id="background">
	
    <div id="underlinemenu">
        <ul>
            <li><a href="http://www.cs.ucy.ac.cy/thesis/smartscan/index.php" title="Home">Home</a></li>
            <li><a href="http://www.cs.ucy.ac.cy/thesis/smartscan/contact.php" title="Contact">Contact</a></li>
            <li><a href='./apk/smartScan.apk' title="Application">Download Application</a></li>
            
           If user was logged in, extra choises apearing: MyBooks & Logout
            <?php
			if($username && $password)
			{
			?>
             <li><a href='userProfile.php' title="MyBooks">My Books</a></li>
             <li><a href='logout.php' title="Logout">Logout</a></li>
            <?php	
			}	
			?>
           
        </ul>
    </div>-->

<!-- CHECK why page4???? -->
<body id="page4">
	<div class="main">

		<!-- Webpage header -->
		<header>
			<div class="wrapper">
				<h1>
					<!-- Project logo, Leads to our homepage -->
					<a href="index.php" id="logo">SmartLib</a>
				</h1>
				<!-- Search Utility on up left -->
				<form id="search" action="#" method="post">
					<div class="bg">
						<input type="submit" class="submit" value=""> <input type="text"
							class="input" id="search_cd"
							onkeydown="doSearch(arguments[0]||event)" />
					</div>
				</form>

			</div>
			
			<nav>
				<!-- Main Menu of Webpage
				CHECK full path was used: http://www.cs.ucy.ac.cy/thesis/smartscan/contact.php"
				CHANGE download lead to android market and in that option has img. put img on most options!
				 -->

				<ul id="menu">
					<li class="alpha"><a href="index.php"><span><span>Home</span> </span>
					</a></li>
					<li><a href="http://www.cs.ucy.ac.cy/thesis/smartscan/contact.php"><span><span>Contact
									Us</span> </span> </a></li>
					<li><a href='./apk/smartScan.apk' class="button">Download Scanner<img
							src="images/android_phone.png" width="40" height="40" alt="test"
							class="android"> </span> </span>
					</a></li>
					<!-- Show options according to user status 
					CHECK why those strange PHP open and close????????????????
					-->
					<?php 
					if(!$username && !$password)
					{
						?>
					<li><a href='register.php'><span><span>Register</span> </span> </a>
					</li>
					<li class="omega"><a href='site_login.php'><span><span>Login</span>
						</span> </a></li>
					<?php 
					}
					else
					{
						?>
					<li><a
						href='userProfile.php'><span><span>My
									Books</span> </span> </a></li>
					<li class="omega"><a
						href='logout.php'><span><span>LogOut</span>
						</span> </a></li>
					<?php
					}
					?>
				</ul> <!-- end of menu -->
			</nav>
			
			<!-- CHECK why this useless javascript???? -->
			<script type="text/javascript"></script>

			<div class="wrapper">

				<div class="perithorio">
					<span class="text1"></span></span>
				</div>

				<!--<div class="text">
				<span class="text1">University of Cyprus<span>online library</span></span>
				<a href='./apk/smartScan.apk' class="button">Download<img src="images/android_phone.png" width="40" height="40" alt="test" class="android"></a>
			</div> -->
			</div>
		</header>
		<div class="ic"></div>
		<!-- / header -->
		<!-- content -->
		<section id="content">
			<div class="wrapper">
				<div class="wrapper">
					<center>

						<span class="clear">&nbsp;</span>
						<div id="screenshots"></div>

						<div id="pager2"></div>

					</center>
				</div>
				<div class="wrapper">
					<div class="box bot pad_bot2">
						<div class="pad">
							<article class="col1">
								<!-- search box -->