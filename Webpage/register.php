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

//User is logged in move him to its Profile Page
if($_SESSION['loggedin']==1){
	header("Location: userProfile.php"); 
}

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
				<h1>Please fill up the Registration Form</h1>
			</header>
			<p>
            
                                        <?php
										//Suppose now there are now activation errors
										unset($_SESSION['actMessage']);
										//If there are errors, display them
										if($_SESSION['regHasErrors']==1){
											
											echo "<span class=\"p4Error\">Errors:</br><strong>";
											
											echo $_SESSION['regMessage'];
										
											echo "</strong></br>";
											
											//Assume errors may resolved in next try
											$_SESSION['regHasErrors']=0;
                                            }
										?>	
                                <!--TODO action-->
								<form id="register-form" action="mobile/SQLregister.php" method="post"
									enctype="multipart/form-data">
									<fieldset>
                                    
										<label><span class="
												<?php if($_SESSION['errUsername']== "1")
														echo "errorSmall";
													else
														echo "p4Black";
														 ?>
                                                         ">Username:</br></span>
                                        <input value="<?php
                                                    echo $_SESSION['REGusername'];
													?>" 
													<?php if($_SESSION['errUsername']== "1")
														echo "class=\"borderRegError\"";
														 ?>
                                         name="username" type="text" />
                                        </label>	</br>
                                        
                                        <label><span class="
										<?php if($_SESSION['errPassword']== "1"
										|| $_SESSION['errMatchPassword'] == "1"
										)
														echo "errorSmall";
													else
														echo "p4Black";
														 ?>
                                                         ">Password:</br></span>
                                        <input 
													<?php if($_SESSION['errPassword']== "1"
													|| $_SESSION['errMatchPassword'] == "1"
													)
														echo "class=\"borderRegError\"";
														 ?>
                                        
                                        name="password" type="password" />
                                        </label></br>
                                        
                                        
                                        <label><span class="
                                        <?php if($_SESSION['errConfPassword']== "1"
										|| $_SESSION['errMatchPassword'] == "1"
										)
														echo "errorSmall";
													else
														echo "p4Black";
														 ?>
                                        ">Confirm Password:</br></span>
                                        <input 
													<?php if($_SESSION['errConfPassword']== "1"
													|| $_SESSION['errMatchPassword'] == "1"
													)
														echo "class=\"borderRegError\"";
														 ?>
                                        name="confPassword" type="password" />
                                        </label></br>
                                        
                                        
                                        <label><span class="
                                        <?php if($_SESSION['errName']== "1")
														echo "errorSmall";
													else
														echo "p4Black";
														 ?>
                                        ">Name:</br></span>
                                        <input value="<?php
                                                    echo $_SESSION['REGname'];
													?>" 
													<?php if($_SESSION['errName']== "1")
														echo "class=\"borderRegError\"";
														 ?>
                                        name="name" type="text" />
                                        </label></br>
                                        
                                        
                                        <label><span class="
                                         <?php if($_SESSION['errSurname']== "1")
														echo "errorSmall";
													else
														echo "p4Black";
														 ?>
                                        ">Surname:</br></span>
                                        <input value="<?php
                                                    echo $_SESSION['REGsurname'];
													?>" 
													<?php if($_SESSION['errSurname']== "1")
														echo "class=\"borderRegError\"";
														 ?>
                                        name="surname" type="text" />
                                        </label></br>
                                        
                                        
                                        <label><span class="
                                          <?php if($_SESSION['errEmail']== "1")
														echo "errorSmall";
													else
														echo "p4Black";
														 ?>
                                        ">Email:</br></span>
                                        <input value="<?php
                                                    echo $_SESSION['REGemail'];
													?>" 
													<?php if($_SESSION['errEmail']== "1")
														echo "class=\"borderRegError\"";
														 ?>
                                        name="email" type="email" />
                                        </label></br>
                                        
                                        
                                        <label><span class="
                                         <?php if($_SESSION['errTelephone']== "1")
														echo "errorSmall";
													else
														echo "p4Black";
														 ?>
                                        ">Telephone:</br></span>
                                        <input value="<?php
                                                    echo $_SESSION['REGtelephone'];
													?>" 
													<?php if($_SESSION['errTelephone']== "1")
														echo "class=\"borderRegError\"";
														 ?>
                                        name="telephone" type="text" />
                                        </label></br>
                                        
                                        
                                        <label><span class="p4Black">Application Notifications:</br></span>
                                        <input name="appNotif" type="checkbox" checked="CHECKED" />
                                        </label></br>
                                         <label><span class="p4Black">Email Notifications:</br></span>
                                        <input name="emailNotif" type="checkbox" />
                                        </label>
                                         </br>
                                       

										
                                   
											
                                              
											<a class="button" href="#"
									onClick="document.getElementById('register-form').submit()">Submit</a>
                                    </br></br></br>
										<a class="button"
												<onClick="showAlert();"
                                                href="scripts/resetRegister.php"
                 								>Clear&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
            
									</fieldset>
								</form>
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