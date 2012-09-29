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
if ($_SESSION['loggedin'] == 0) {
    header("Location: register.php");
}

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
    <link href="images/favicon.ico" rel="icon" type="image/x-icon"/>
    <!--    Include Scripts-->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <link href=styles.css rel=stylesheet>
    </link>
    <!--    Include Extra headers-->
    <?php include 'scripts/gridHeader.php';
    include 'carousel/carouselHeader.php';  ?>

    <script src="scripts/mainScripts.js"></script>
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
            <header>
                <h1><?php echo $_SESSION['name']?>, your books</h1>
            </header>
            <div class="mainContent">
                <p>
                <center>
                    <?php include 'grid/userBooks.php'; ?></center>

                <br><br>
                </p>
            </div>
            <header>
                <h1>
                    Your books in Bibtex Format:
                </h1>
            </header>
            <div class="mainContent">
                <p>Take your books, in Bibtex format and include them on your webpage:</p>
                <textarea id="bibtexBook"></textarea>
                <?php
                echo '<script>' .
                    'var user="' . $_SESSION['username'] . '";' .
                    '</script>';
                ?>

                <script>
                    var params = "user=" + user + "&mykey=0a05bb77edad7395da818f361f8115a2";
                    runAWebpageForResult("scripts/exportUserBooks.php", params, fillBibtexBooks);
                </script>
                <br><br>
            </div>
        </article>

    </div>


</div>

<footer class=clearfix>
    <?php include "scripts/footer.php" ?>
</footer>

</body>
</html>