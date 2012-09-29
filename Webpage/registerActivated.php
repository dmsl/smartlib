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

//If there are errors, or user skipped the Registration form
if ($_SESSION['regHasErrors'] != "0"
    || !isset($_SESSION['regHasErrors'])
) {
    //And didnt user email activation link
    if ($_SESSION['LnkLegalActivation'] != "Y")
        //Dont allow him to view this page
        die("Permission denied.");
}

//User is logged in move him to its Profile Page
if ($_SESSION['loggedin'] == 1) {
    header("Location: userProfile.php");
}

?>


<!DOCTYPE html>
<html lang=en>
<head>
    <meta charset=UTF-8>
    <title>SmartLib</title>
    <link href=styles.css rel=stylesheet/>
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
                    if ($_SESSION['loggedin'] == 1) {
                        ?>
                        <li><a href="userProfile.php"><?php echo $_SESSION['name']?> Profile</a>
                        </li>
                        <?php
                    } //User is Guest
                    else {
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
                <h1><?php echo $_SESSION['REGname']?>, you have successfully activated your account!</h1>
            </header>


            </br></br>
            <h4blue>You can now login to smartlib!</h4blue>
            <form id="register-form" action="mobile/activateAccount.php" method="post"
                  enctype="multipart/form-data">
                <fieldset>
                    </br>
                    <label><span class="p4Black">Username:&nbsp;&nbsp;</span>
                        <span class="p4Blue"><?php echo $_SESSION['REGusername']?></br></span>
                    </label></br>

                    <label><span class="p4Black">Password:&nbsp;&nbsp;</span>
                        <span class="p4Blue"><?php echo $_SESSION['REGpassword']?></br></span>
                    </label></br>


                    <label><span class="p4Black">Name:&nbsp;&nbsp;</span>
                        <span class="p4Blue"><?php echo $_SESSION['REGname']?></br></span>
                    </label></br>


                    <label><span class="p4Black">Surname:&nbsp;&nbsp;</span>
                        <span class="p4Blue"><?php echo $_SESSION['REGsurname']?></br></span>
                    </label></br>


                    <label><span class="p4Black">Email:&nbsp;&nbsp;</span>
                        <span class="p4Blue"><?php echo $_SESSION['REGemail']?></br></span>
                    </label></br>


                    <label><span class="p4Black">Telephone:&nbsp;&nbsp;</span>
                        <span class="p4Blue"><?php echo $_SESSION['REGtelephone']?></br></span>
                    </label></br>


                    <label><span class="p4Black">Application Notifications:&nbsp;&nbsp;</span>
                                        <span class="p4Blue"><?php if ($_SESSION['REGappNotif'] == "on")
                                            echo "Yes"; else echo "No";?></br></span>
                    </label></br>
                    <label><span class="p4Black">Email Notifications:&nbsp;&nbsp;</span>
                                        <span class="p4Blue"><?php if ($_SESSION['REGemailNotif'] == "on")
                                            echo "Yes"; else echo "No";?></br></span>
                    </label></br>


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