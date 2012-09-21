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
    <meta name="keywords" content="SmartLib, Smart, Library, Books, UCY,
    University Of Cyprus, CrowdSourcing"/>
    <title>SmartLib</title>

    <!--    Include Scripts-->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>

    <link href=styles.css rel=stylesheet />

    <!--    Include Extra headers-->
    <?php include 'scripts/gridHeader.php';
    include 'carousel/carouselHeader.php';  ?>


    <script>
        var isShown = false;

//        //Slide top menu
//        $(document).ready(function () {
//            $('#sub-menu-panel').click(function () {
//                //Show panel
//                if (!isShown) {
//                    $('#login-panel').slideDown("slow");
//                    isShown = true;
//                }
//                //Hide panel
//                else {
//
//
//                    $('#login-panel').slideUp("slow");
//                    isShown = false;
//                }
//
//
//            });
//            $('#login-panel').click(function () {
//
//
//            });
//
//
//        });

        function toggleLoginButton() {
            if (($('#loginUsernameField').attr('value') != "")
                    &&
                    ($('#loginPasswordField').attr('value') != "")
                    ) {
                //Activate Login Link
                //              //            //Login is link class=\"loginButton\"
/*                $('#login-panel-submit').replaceWith("aaaaaaaaaa <a  id=\"login-panel-submit\" \
                      href=\"#\" onClick=\"document.getElementById('login-widget').submit() >Login</a>");*/

                $('#login-panel-submit').replaceWith(
                        '<a  id="login-panel-submit-filled" href="#"  onClick="document.getElementById("login-widget").submit()" >Login</a>');




            }
            else {
                //Deactivate Login Link
                //Login is not link
                $('#login-panel-submit-filled').replaceWith(
                        "<div id=\"login-panel-submit\">Login</div>");
            }

        }


        //Add extra functionality to password
        function toggleLoginButtonPassword(event) {
            //Toggle Login Button
            toggleLoginButton();
            if (event.keyCode == 13) {
               // $('#login-panel-submit-filled').click();
                document.getElementById("login-widget").submit()
            }


        }


    </script>


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

    <!--LOGIN INPUT USER & PASS -->
    <!--Webpage Title: SmartLib .. -->
    <form id="login-widget" action="mobile/authenticate.php" method="post"
    autocomplete="off"
          enctype="multipart/form-data">
        <fieldset>
            <div id="login-panel">
                <input type="text" name="username" placeholder="Username"
                       autocomplete="off"
                       class="loginInput" id="loginUsernameField"
                       onkeyup="toggleLoginButton()"
                        />

                <input type="password" name="password" placeholder="Password"
                       class="loginInput"
                       onkeyup="toggleLoginButtonPassword(event)"
                       id="loginPasswordField"
                       autocomplete="off"  />
                <!--        Login Button-->
                <div id="login-panel-submit">Login</div>
            </div>
        </fieldset>
    </form>


    <a class="imageHandle" href="index.php">
        <img height="100"
             align="left" src="images/logo.png"/>
    </a>


</header>


<div class=clearfix>


    <div id=content>
        <!--    If user is logged in-->
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