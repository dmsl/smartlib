<?php 

/*
 This file is part of SmartLib Project.

SmartLib is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SmartLib is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.


Author: Paschalis Mpeis

Affiliation:
Data Management Systems Laboratory
Dept. of Computer Science
University of Cyprus
P.O. Box 20537
1678 Nicosia, CYPRUS
Web: http://dmsl.cs.ucy.ac.cy/
Email: dmsl@cs.ucy.ac.cy
Tel: +357-22-892755
Fax: +357-22-892701


*/

session_start();
include 'scripts/sessionInit.php';
?>
<!DOCTYPE html>
<html lang=en>
<head>
	<meta charset=UTF-8>
	<title>SmartLib</title>
	<link href=styles.css rel=stylesheet />
</head>

<body>
<header>

   <section>
   <?php include 'scripts/header.php'; ?>
		</section>
		

     <aside>
     <?php include 'scripts/smartWidget.php'; ?>
     
</aside>
	

		
</header>

<div class=clearfix>
	<div id=content>
<!--    If user is logged in-->
		<article>
        	<nav id=global>
		<ul>
			<li><a href="index.php">Home</a></li>
                             <?php 
							//User is logged in
							if($_SESSION['loggedin']==1){
								?>
								<li><a href="userProfile.php"><?php echo $_SESSION['name']?> Profile</a>
								</li>
                                <?php
								}
							//User is Guest
							else{
								?>
								
								<li><a href="register.php">Register</a>
								</li>
                                <?php
								}
								?>
			<li><a href="downloads.php">Downloads</a></li>
			<li><a href="about.php">About</a></li>
   			<li><a href="contact.php">Contact</a></li>
		</ul>
	</nav>
			<header>	
				<h1>Download SmartLib on your Smartphone or Tablet</h1>
			</header>
			<p>
            SmartLib is available for Android and iOS Platform. Using SmartLib application you
            can have access to all activated libraries.<br>
            Additionally you can:<br>
            add books to your library, make them rentable or not, lent them,
            request books owned by other users, contact u users.<br>
            <a class="button" href="https://play.google.com"
									>Android</a><br><br>
                               <a class="button" href="http://itunes.apple.com">iOS
                               &nbsp;&nbsp;&nbsp;&nbsp;
                               &nbsp;&nbsp;&nbsp;</a> <br><br>
                                    

 
                                      <br>
                                      <blockquote>
										<img src="http://www.cs.ucy.ac.cy/projects/smartLib/images/qr.png"  alt="Smart QR Code"/>
                                        </blockquote>
                                        </p>
			<header>
				<h1>Setup your own Library</h1>
			</header>
			<p>
           <strong>Requirements:</strong> PHP Server, MySQL Database.<br>
            <strong>Optional:</strong> Mail Server, for users private intercomunication.<br>
            Download the SmartLib Open Source Project from GitHub, and follow instructions
            in readme file. Once the installation process completes, you have to wait
            for your library's activation from SmartLib Database Administrator.
			<br><span class="imageHandle">
            <a href="https://github.com/dmsl/smartlib" target="_blank"><img weigth="50" src="images/github.png" alt="SmartLib Repository" ></a></br></span>	
            </p>
		</article>
			
	</div>
	

</div>

<footer class=clearfix>
<?php include "scripts/footer.php" ?>

</footer>

</body>
</html>