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
    <link href=styles.css rel=stylesheet>
    </link>
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
            <header>
                <h1>Contact <?php echo _NAME; ?></h1>
            </header>
            <div class="mainContent">
                <p>
                    <aside>
                        <h1>
                            <center>Office Information</center>
                        </h1>
                        <strong>Address:</strong><br>
                        <?php echo _ADDRESS; ?><br>
                        <strong>Telephone: </strong><?php echo _TELEPHONE; ?>
                        <br>                                        <?php if (defined("_FAX")) {

                        echo "<strong>Fax: </strong> " . _FAX . "<br>";
                    } ?>

                        <strong>Email:</strong><a target="_blank" href=
                        <?php echo "\"mailto:" . _EMAIL . "\"";?>>
                        <?php echo _EMAIL; ?></a>
                    </aside>
                <form id="contact-form" action="scripts/contactSubmit.php" method="post"
                      enctype="multipart/form-data">
                    <fieldset>
                        <label><span class="text-form">Name:</span><input name="CONTname"
                                                                          type="text"
                                                                          value="<?php echo $_SESSION['CONTname'];  ?>"/>
                        </label><br>
                        <label><span class="text-form">Email:</span><input
                                name="CONTemail" type="text"
                                value="<?php echo $_SESSION['CONTemail'];  ?>"/> </label>

                        <div class="wrapper">
                            <label>
                                <div class="text-form">Message:</div>
                                <textarea cols=40 rows=7 name="CONTmessage"><?php
                                    echo $_SESSION['CONTmessage'];
                                    ?></textarea>
                            </label>
                        </div>

                        <br>
                        <a class="button" href="#"
                           onClick="document.getElementById('contact-form').submit()">Send</a>
                        <br><br><a class="button" href="scripts/resetContact.php"
                            >Clear</a>
                    </fieldset>
                </form>

            </div>
        </article>

    </div>


</div>

<footer class=clearfix>
    <?php include "scripts/footer.php" ?>
</footer>

</body>
</html>