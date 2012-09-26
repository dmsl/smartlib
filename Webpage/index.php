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

<!--        Head         -->
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


<body>
<!-- Header -->
<header id='topHeader'>
    <?php  include('scripts/header.php'); ?>
</header>
<!--Toast Messages -->
<div class=clearfix>
    <div class="toast-message" id="notoast">No Errors Found</div>
<div id=content>
        <article>
            <!--            Show popular books at all times-->
            <header>
                <h1>Popular Books</h1>
            </header>
            <div class="mainContent" >
                <ul id="mycarousel" class="jcarousel-skin-ie7">
                    <ul>
                        <!-- The content will be dynamically loaded in here -->
                    </ul>
                </ul>
                </div>
            <?php
            //User is logged in
            if ($_SESSION['loggedin'] == 1) {

                ?>


                <header>
                    <h1>All Books</h1>
                </header>
                <div class="mainContent" >
                    <?php include 'grid/allBooksLoggedIn.php'; ?></center>
                        </div>
                <br>

                <?php
            } else {
                ?>


                <header>
                    <h1>All Books</h1>
                </header>
                <div class="mainContent" >
                    <?php include 'grid/allBooks.php'; ?></center>
                        </div>
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