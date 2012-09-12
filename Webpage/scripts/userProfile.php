<!-- Initialize Session -->
<?php include 'sessionInit.php'; ?>
<?php 
require_once 'grid/tabs.php'; ?>

<?php 
//User is logged in move him to its Profile Page
if($_SESSION['loggedin']==0){
	header("Location: register.php"); 
}
?>


<!-- Include the Header of webpage -->
<?php include 'header.php'; ?>
<!DOCTYPE html>
<html lang="en">
<head>
<title></title>
<meta charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css"
	media="screen">
<link rel="stylesheet" href="css/style.css" type="text/css"
	media="screen">
<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen">
    


 <link rel="stylesheet" type="text/css" media="screen" href="grid/themes/pasqtheme/p.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="grid/themes/ui.jqgrid.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="grid/themes/ui.multiselect.css" />
    <style type="text">
        html, body {
        margin: 0;			/* Remove body margin/padding */
    	padding: 0;
        overflow: hidden;	/* Remove scroll bars on browser window */
        font-size: 75%;
        }
    </style>
    <script src="grid/js/jquery.js" type="text/javascript"></script>
    <script src="grid/js/i18n/grid.locale-en.js" type="text/javascript"></script>
	<script type="text/javascript">
	$.jgrid.no_legacy_api = true;
	$.jgrid.useJSON = true;
	</script>
    <script src="grid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
    <script src="grid/js/jquery-ui-custom.min.js" type="text/javascript"></script>
    


    
    
<!--<script type="text/javascript" src="js/jquery-1.6.min.js"></script>-->
<script src="js/cufon-yui.js" type="text/javascript"></script>
<script src="js/cufon-replace.js" type="text/javascript"></script>
<script src="js/Open_Sans_400.font.js" type="text/javascript"></script>
<script src="js/Open_Sans_Light_300.font.js" type="text/javascript"></script>
<script src="js/Open_Sans_Semibold_600.font.js" type="text/javascript"></script>
<script src="js/FF-cash.js" type="text/javascript"></script>

<!--[if lt IE 7]>
	<div style=' clear: both; text-align:center; position: relative;'>
		<a href="http://www.microsoft.com/windows/internet-explorer/default.aspx?ocid=ie6_countdown_bannercode"><img src="http://www.theie6countdown.com/images/upgrade.jpg" border="0"  alt="" /></a>
	</div>
<![endif]-->
<!--[if lt IE 9]>
	<script type="text/javascript" src="js/html5.js"></script>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen">
<![endif]-->
</head>
<body id="page2">
	<!-- header -->
	<div class="bg">
		<div class="main">
			<header>
				<div class="row-1">
					<h1>
						<!-- Include Logo -->
						<?php include 'smartlibLogo.php'; ?>
					</h1>
										<!-- Include Smart Widget Widget -->
					<?php include 'smartWidget.php'; ?>
				</div>

				<!-- Include the Main Menu of webpage -->
				<div class="row-2">
					<nav>
						<ul class="menu">
							<li><a href="index.php">Home</a>
							</li>
							<li><a class="active"  href="userProfile.php">User Profile</a>
							</li>
							<li><a href="downloads.php">Downloads</a>
							</li>
							<li><a href="aboutSmartlib.php">About SmartLib</a>
							</li>
							<li class="last-item"><a href="contact.php">Contact Us</a>
							</li>
						</ul>
					</nav>
				</div>

			</header>
			<!-- content -->
			<!--<div class="ic">More Website Templates at TemplateMonster.com!</div>-->
			<section id="content">
				<div class="padding">
					<div class="wrapper p4">
						
							<div class="indent">
								
								<h2><?php echo $_SESSION['name']?> Profile</h2>
                                <h3>Manage your Books:</h3>
								<!--  Put here table BookOfUser -->
                     	<?php include ("grid/booksOfUser.php");?>

						</div>
						
					</div>
					<div class="box-bg">
						  <?php include "aboveFooter.php" ?>
					</div>
				</div>
			</section>
			<!-- footer -->
			<footer>
				<div class="row-top">
					 <?php include 'footer.php'; ?>
				</div>
				<div class="row-bot">
					<div class="aligncenter">
						<!-- Include Bottom Links -->
                        <?php include 'bottomLinks.php'; ?>
						<!-- {%FOOTER_LINK} -->
					</div>
				</div>
			</footer>
		</div>
	</div>
	<script type="text/javascript"> Cufon.now(); </script>
</body>
</html>
