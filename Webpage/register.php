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
if ($_SESSION['loggedin'] == 1) {
    header("Location: userProfile.php");
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
                <h1>Please fill up the Registration Form</h1>
            </header>
            <div class="mainContent" id="registrationForm">
                <?php
                //Suppose now there are now activation errors
                unset($_SESSION['actMessage']);
                //If there are errors, display them
                if ($_SESSION['regHasErrors'] == 1) {

                    echo "<span class=\"p4Error\">Errors:</br><strong>";

                    echo $_SESSION['regMessage'];

                    echo "</strong></br>";

                    //Assume errors may resolved in next try
                    $_SESSION['regHasErrors'] = 0;
                }
                ?>
                <!--TODO action-->


                <div id="register-panel">

                    <form id="register-form" action="mobile/SQLregister.php" method="post"
                          enctype="multipart/form-data">
                        <fieldset>
                            <label><span class="
												<?php if ($_SESSION['errUsername'] == "1")
                                echo "errorSmall";
                            else
                                echo "p4Black";
                            ?>
                                                         ">Username:</br></span>
                                <input value="<?php
                                echo $_SESSION['REGusername'];
                                ?>"
                                    <?php if ($_SESSION['errUsername'] == "1")
                                    echo "class=\"borderRegError\"";
                                    ?>
                                       name="username" type="text"/>
                            </label>

                            <label><span class="
										<?php if ($_SESSION['errPassword'] == "1"
                                || $_SESSION['errMatchPassword'] == "1"
                            )
                                echo "errorSmall";
                            else
                                echo "p4Black";
                            ?>
                                                         ">Password:</br></span>
                                <input
                                    <?php if ($_SESSION['errPassword'] == "1"
                                    || $_SESSION['errMatchPassword'] == "1"
                                )
                                    echo "class=\"borderRegError\"";
                                    ?>

                                        name="password" type="password"/>
                            </label>


                            <label><span class="
                                        <?php if ($_SESSION['errConfPassword'] == "1"
                                || $_SESSION['errMatchPassword'] == "1"
                            )
                                echo "errorSmall";
                            else
                                echo "p4Black";
                            ?>
                                        ">Confirm Password:</br></span>
                                <input
                                    <?php if ($_SESSION['errConfPassword'] == "1"
                                    || $_SESSION['errMatchPassword'] == "1"
                                )
                                    echo "class=\"borderRegError\"";
                                    ?>
                                        name="confPassword" type="password"/>
                            </label>


                            <label><span class="
                                        <?php if ($_SESSION['errName'] == "1")
                                echo "errorSmall";
                            else
                                echo "p4Black";
                            ?>
                                        ">Name:</br></span>
                                <input value="<?php
                                echo $_SESSION['REGname'];
                                ?>"
                                    <?php if ($_SESSION['errName'] == "1")
                                    echo "class=\"borderRegError\"";
                                    ?>
                                       name="name" type="text"/>
                            </label>


                            <label><span class="
                                         <?php if ($_SESSION['errSurname'] == "1")
                                echo "errorSmall";
                            else
                                echo "p4Black";
                            ?>
                                        ">Surname:</br></span>
                                <input value="<?php
                                echo $_SESSION['REGsurname'];
                                ?>"
                                    <?php if ($_SESSION['errSurname'] == "1")
                                    echo "class=\"borderRegError\"";
                                    ?>
                                       name="surname" type="text"/>
                            </label>


                            <label><span class="
                                          <?php if ($_SESSION['errEmail'] == "1")
                                echo "errorSmall";
                            else
                                echo "p4Black";
                            ?>
                                        ">Email:</br></span>
                                <input value="<?php
                                echo $_SESSION['REGemail'];
                                ?>"
                                    <?php if ($_SESSION['errEmail'] == "1")
                                    echo "class=\"borderRegError\"";
                                    ?>
                                       name="email" type="email"/>
                            </label>


                            <label><span class="
                                         <?php if ($_SESSION['errTelephone'] == "1")
                                echo "errorSmall";
                            else
                                echo "p4Black";
                            ?>
                                        ">Telephone:</br></span>
                                <input value="<?php
                                echo $_SESSION['REGtelephone'];
                                ?>"
                                    <?php if ($_SESSION['errTelephone'] == "1")
                                    echo "class=\"borderRegError\"";
                                    ?>
                                       name="telephone" type="text"/>
                            </label>


                            <label><span class="p4Black">Application Notifications:</br></span>
                                <input name="appNotif" type="checkbox" checked="CHECKED"/>
                            </label>
                            <label><span class="p4Black">Email Notifications:</br></span>
                                <input name="emailNotif" type="checkbox"/>
                            </label>
                            </br>


                            <a class="button" href="#"
                               onClick="document.getElementById('register-form').submit()">Submit</a>
                            </br></br></br>
                            <a class="button"
                            <onClick
                            ="showAlert();"
                            href="scripts/resetRegister.php"
                            >Clear&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>

                        </fieldset>
                    </form>
                </div>

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