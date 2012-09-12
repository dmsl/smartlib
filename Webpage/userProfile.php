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

//TODO require_once 'grid/tabs.php'; 

//User is logged in move him to its Profile Page
if($_SESSION['loggedin']==0){
	header("Location: register.php"); 
}

?>
<!DOCTYPE html>
<html lang=en>
<head>
	<meta charset=UTF-8>
	<title>SmartLib</title>
	<link href=styles.css rel=stylesheet />
       <?php include 'scripts/gridHeader.php'; ?>
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
				<h1><?php echo $_SESSION['name']?>, your books</h1>
			</header>
			<p>
                    <center>
            <?php include 'grid/userBooks.php'; ?></center>
            
                <br><br>
                </p>
                <header>
                <h1><center>Export your books in Bibtext Format:</center></h1></header>
                <p>To export your books in bibtext format, you have to build and use the following URL:
                <br><br>
                http://www.cs.ucy.ac.cy/projects/smartLib/scripts/exportUserBooks.php?user=<span
                class="errorSmall">USERNAME</span>&pass=<span 
                class="errorSmall">PASSWORD</span>
                <br>and replace <span
                class="errorSmall">USERNAME</span> and <span 
                class="errorSmall">PASSWORD</span> with your SmartLib Credencials.
                <!--<a class="button" target="_blank"
				href="scripts/exportBooks.php" >Export your Books</a>   -->    
                                
            </p>
			<br><br>

		</article>
			
	</div>
	

</div>

<footer class=clearfix>
<?php include "scripts/footer.php" ?>

</footer>

</body>
</html>