<?php

	session_start();
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>SmartScan</title>

<link href="css/main.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="css/frontpage.css"/>

<link href="./css/fancybox.css" rel="stylesheet" type="text/css" media="screen">


<!--[if IE 6]>

	  <link rel="Stylesheet" rev="Stylesheet" href="./css/frontpage.ie6.css" type="text/css" media="screen" />

	<![endif]-->

<!--[if IE 7]>

	  <link rel="Stylesheet" rev="Stylesheet" href="./css/frontpage.ie7.css" type="text/css" media="screen" />

	<![endif]-->

<link href="./css/jquery.jcarousel.css" rel="stylesheet" type="text/css" media="screen">

<link href="./css/jquery.jcarousel.skin.css" rel="stylesheet" type="text/css" media="screen">

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

<link href="./css/ui.jqgrid.css" rel="stylesheet" type="text/css" media="screen">

<link href="./css/jquery-ui-1.8.18.custom.css" rel="stylesheet" type="text/css" media="screen">

<link rel="stylesheet" type="text/css" href="css/tipsy.css"/>

<script type="application/javascript" src="js/jquery.tipsy.js"></script>

<link rel="stylesheet" type="text/css" href="stars/rating.css"/>

<script type="application/javascript" src="stars/jquery.rating.js"></script>

<script type="text/javascript">

	$.jgrid.no_legacy_api = true;

	$.jgrid.useJSON = true;
	

</script>

</head>



<body>

<div id="logo"> <a href="index.php"><img src="images/Smartscan.png" width="128" height="128" background-attachment="fixed"/></a> </div>

<div class="centered">

  <div id="search" class="right major_column">

    <form method="post" action="./search.php">

      <div class="searchbox">

        <input type="hidden" name="language" value="en"/>

        <select id="searchscope" name="searchscope" style="width: 30px; top: -1000px; left: -1000px; position: absolute; ">

          <option value="isbn">search by ISBN</option>

          <option value="author">search by Author</option>

          <option value="title">search by Title</option>

        </select>

        <input type="text" name="searchstring" class="textfield transformed"/>

        <input type="image" alt="run search" title="run search" class="searchbutton" src="./images/magnifier.png" style="display: inline; "/>

        <input type="submit" name="searchbutton" class="button" value="run search" style="display: none; "/>

      </div>

    </form>

  </div>

</div>

<!-- search box -->

<span class="clear">&nbsp;</span>
<?php

	$username = $_SESSION['username'];
	$password = $_SESSION['password'];


?>
<div id="background">
	
    <div id="underlinemenu">
        <ul>
            <li><a href="http://www.cs.ucy.ac.cy/thesis/smartscan/index.php" title="Home">Home</a></li>
            <li><a href="http://www.cs.ucy.ac.cy/thesis/smartscan/contact.php" title="Contact">Contact</a></li>
            <li><a href='./apk/smartScan.apk' title="Application">Download Application</a></li>
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
    </div>

  <?php
	

	

	//Check do we have username and password



	if(!$username && !$password)

	{

	echo "Welcome Guest! <br> <a href=site_login.php>Login</a> | <a href=register.php>Register</a>";
    
	}

	else

	{

	echo "Welcome ".$username."";
	}	

?>

  <center>

    <span class="clear">&nbsp;</span> <span class="centered" id="screenshots" style="display: hidden; "> </span>

    <div id="pager2"></div>

  </center>

  <br/>

</div>

<div id="toggle_screenshots_container" class="centered"> <a href="#" id="toggle_screenshots" class=""><span>↓Top Rated Books</span></a> </div>

<script type="text/javascript">
	jQuery(document).ready(function(){
		$('#toggle_screenshots').trigger('click');
	});
</script>

<div id="mainContent">