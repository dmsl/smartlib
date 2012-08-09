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
<!--    Include Extra headers-->
   <?php include 'scripts/gridHeader.php'; ?>
   
                <?php 
							//User is logged in
							if($_SESSION['loggedin']==1){
								//Show popular books

								include 'carousel/carouselHeader.php';
								}
							
								?>

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
			
            
             <?php 
							//User is logged in
							if($_SESSION['loggedin']==1){
								//Show popular books
								?>
                                <header>	
								<h1>Popular Books</h1>
                                </header>
                                
                               <center>
                                  <ul id="mycarousel" class="jcarousel-skin-ie7">
                                    <ul>
                                      <!-- The content will be dynamically loaded in here -->
                                    </ul>
                                  </ul>
                                    </center>
                                    <header>
                                    <h1>All Books</h1>
                                </header>
                                <center>
                                <?php include 'grid/allBooksLoggedIn.php'; ?></center>
                                <br>
                              
                                <?php
								}
								else
								{
								?>
            
				
                            <header>
                                <h1>All Books</h1>
                            </header>
                            <center>
                            <?php include 'grid/allBooks.php'; ?></center>
                            <br>
                             <?php } ?>
        
		</article>
			
	</div>
	

</div>

<footer class=clearfix>
<?php include "scripts/footer.php" ?>

</footer>

</body>
</html>