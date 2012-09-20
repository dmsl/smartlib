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

    //Include carousel header
    include 'carousel/carouselHeader.php';    ?>
</head>

<script src="jquery.js"></script>
<script>
    var isShown=false;

    //Slide top menu
    $(document).ready(function(){
        $('#sub-menu-panel').click (function(){
            //Show panel
            if(!isShown){
                //Login is not link
                $('#login-panel').slideDown("slow",function(){
                    $('#sub-menu-panel-login-link').replaceWith(
                            "<a class=\"loginButton\" id=\"sub-menu-panel-login-link\" \
                           href=\"#\" onClick=\"document.getElementById('login-widget').submit()\">Login</a>");

                });
                isShown=true;
            }
            //Hide panel
            else{


                $('#login-panel').slideUp("slow");
                //Login is not link
                $('#sub-menu-panel-login-link').replaceWith(
                        "<div id=\"sub-menu-panel-login-link\">Login</div>");
                isShown=false;
            }


        });
        $('#login-panel').click (function(){


        });



    });
</script>


<body>
<!-- Header -->
<header>
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
    <form id="login-widget" action="mobile/authenticate.php" method="post"
          enctype="multipart/form-data">
        <fieldset>
    <div id="login-panel">


                <input type="text" name="username" value="Username"   class="loginhint"
                       onBlur="if(this.value=='') {
                       //TODO MAKE THOSE FUNCTIONS
                this.value='Username';
                this.className='loginhint';
                }
                else {
                 this.className='loginfilled';
                 }

                "
                 onFocus="if(this.value =='Username' ){
                 this.value='';

                 }

                 " />
                <input type="text" name="password" value="Password" class="loginhint"
                       onBlur="if(this.value=='') {
                this.value='Password';
                type='text';
                this.className='loginhint';
                }
                else {
                this.className='loginfilled';
                }
                "
                onFocus="if(this.value =='Password' ){
                this.value='';
               	type='password';

                }

                "
                        />


    </div>

    <div id="sub-menu-panel">
        <div id="sub-menu-panel-login-link">Login</div>
    </div>

    </fieldset>
    </form>

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