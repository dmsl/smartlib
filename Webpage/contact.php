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
				<h1>Contact <?php echo _NAME; ?></h1>
			</header>
			<p>
<aside>
										<span><h1><center>Office Information</center></h1></span>
                                        <strong>Address:</strong><br>
                                        <?php echo _ADDRESS; ?><br>
                                        <strong>Telephone: </strong><?php echo _TELEPHONE; ?><br>                                        <?php if(defined("_FAX")){
											
                                           echo "<strong>Fax: </strong> ". _FAX."<br>";
											} ?>
                                        
										<strong>Email:</strong><a target="_blank" href=
                                        <?php echo "\"mailto:"._EMAIL."\"";?>>
                                        <?php echo _EMAIL; ?></a>
									</aside>
								<form id="contact-form" action="scripts/contactSubmit.php" method="post"
									enctype="multipart/form-data">
									<fieldset>
										<label><span class="text-form">Name:</span><input name="CONTname"
											type="text" value="<?php echo $_SESSION['CONTname'];  ?>" />
                                            </label><br>	
                                            <label><span class="text-form">Email:</span><input
											name="CONTemail" type="text"
                                            value="<?php echo $_SESSION['CONTemail'];  ?>" /> </label>
										<div class="wrapper">
											<div class="text-form" >Message:</div>
											<textarea cols=40 rows=7 name="CONTmessage"><?php
                                            echo $_SESSION['CONTmessage'];
											?></textarea>
										</div>
										
											<br>
											<a class="button" href="#"
										onClick="document.getElementById('contact-form').submit()">Send</a>
                                        <br><br><a class="button" href="scripts/resetContact.php"
												>Clear</a>
									</fieldset>
								</form>
                                
		
            </p>
		</article>
			
	</div>
	

</div>

<footer class=clearfix>
<?php include "scripts/footer.php" ?>

</footer>

</body>
</html>