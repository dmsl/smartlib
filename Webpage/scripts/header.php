<!-- 
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

    REMOVE TODO REMOVE T

    -->

<!-- TODO REMOVE THIS FILE -->


<div id="top-panel">
    <div id="top-panel-title">SmartLib: Library of the modern World</div>
</div>

<!-- TODO move this to script to a php, and check if its connected-->
<!--    If it is, show the logged in div, else this-->
<!-- Webpage Menu -->
<nav id=global>
    <ul>
        <li><a href="index.php">Home</a></li>
        <?php
        //User is logged in
        if ($_SESSION['loggedin'] == 1) {
            ?>
            <li><a id="navUserProfile" href="userProfile.php">Profile</a>
            </li>
            <?php
        } //User is Guest
        else {
            ?>

            <li><a id="navRegister" href="register.php">Register</a></li>
            <?php
        }
        ?>
        <li><a href="downloads.php">Downloads</a></li>
        <li><a href="about.php">About</a></li>
        <li><a href="contact.php">Contact</a></li>
    </ul>
</nav>


<!--TODO LIBRARY logo TODO MOVE THIS-->
<a id="imageLogo" href="index.php">
    <img height="100"
         align="left" src="images/logo.png"/>
</a>


<!--Show Login Panel-->
<?php include('scripts/smartWidget.php'); ?>
