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
    <!--    TODO fill metadata for all pages-->
    <meta name="description" content="SmartLib : Library of Modern World"/>
    <meta name="keywords" content="smartLib, smart, library, books, ucy,
    university of cyprus, university, cyprus, cs"/>
    <title>SmartLib</title>

    <!--    Include Scripts-->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <link href=styles.css rel=stylesheet />
    <!--    Include Extra headers-->
    <?php include 'scripts/gridHeader.php';
    include 'carousel/carouselHeader.php';  ?>


    <script src="scripts/loginScript.js"></script>


</head>
<!-- Include Google's JQUery TODO make this for all pages!
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>-->


<body>

<!-- Header -->
<header>


    <div id="top-panel">
        <div id="top-panel-title">SmartLib: Library of the modern World</div>
    </div>


    <!-- Webpage Menu -->
    <nav id=global>
        <ul>
            <li><a href="index.php">Home</a></li>
            <?php
            //User is logged in
            if ($_SESSION['loggedin'] == 1) {
                ?>
                <li><a href="userProfile.php">Profile</a>
                </li>
                <?php
            } //User is Guest
            else {
                ?>

                <li><a href="register.php">Register</a></li>
                <?php
            }
            ?>
            <li><a href="downloads.php">Downloads</a></li>
            <li><a href="about.php">About</a></li>
            <li><a href="contact.php">Contact</a></li>
        </ul>
    </nav>







<!--    TODO if user is logged in show logged in panel, else show this panel-->

        <fieldset>
            <div id="login-panel">
                <input type="text" name="username" placeholder="Username"
                       autocomplete="off"
                       autofocus="autofocus"
                       id="loginUsernameField"
                       onkeyup="toggleLoginButton()"
                        />

                <input type="password" name="password" placeholder="Password"

                       onkeyup="toggleLoginButtonPassword(event)"
                       id="loginPasswordField"
                       autocomplete="off"  />
                <!--        Login Button-->
                <div  id="login-panel-submit">Login</div>
            </div>
        </fieldset>
<!--    </form>-->

<!--TODO LIBRARY logo TODO MOVE THIS-->
    <a class="imageHandle" href="index.php">
        <img height="100"
             align="left" src="images/logo.png"/>
    </a>
</header>


<div class=clearfix>

    <div class="toast-message" id="notoast">No Errors Found</div>


    <div id=content>

        <article>


            <!--            Show popular books at all times-->
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
            <?php
            //User is logged in
            if ($_SESSION['loggedin'] == 1) {

                ?>


                <header>
                    <h1>All Books</h1>
                </header>
                <center>
                    <?php include 'grid/allBooksLoggedIn.php'; ?></center>
                <br>

                <?php
            } else {
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