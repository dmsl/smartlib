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
<html lang=en xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">

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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <link href=styles.css rel=stylesheet>
    </link>
    <!--    Include Extra headers-->
    <?php include 'scripts/gridHeader.php'; ?>

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
            <div class="mainContentCentered" id="booksLoggedIn">
                <?php include 'grid/userBooks.php'; ?>
            </div>
            <br><br>

            <header>
                <h1>
                    Your books exported in Bibtex Format:
                </h1>
            </header>
            <div class="mainContent">
                <textarea id="bibtexBook"></textarea>
                <?php
                echo '<script>' .
                    'var user="' . $_SESSION['username'] . '";' .
                    '</script>';
                ?>

                <script>
                    var params = "user=" + user + "&mykey=" + <?php echo "'" . md5(_MY_KEY) . "'" ?>;
                    runAWebpageForResult(
                    <?php
                    echo '"' . _LIB_URL . '/scripts/exportBooks.php"'; ?>,
                            params, fillBibtexBooks);
                </script>
                <br><br>
            </div>


            <header>
                <h1>
                    Use Smartlib Plugin
                </h1>
            </header>
            <div class="mainContent">


                <?php
                require_once("CONFIG.php"); ?>
                <link <?php
                    echo 'href="' . _LIB_URL . '/scripts/smartPlugin.css"';
                    ?>   rel=stylesheet>
                </link>

                <script <?php
                    echo 'src="' . _LIB_URL . '/scripts/mainScripts.js"';
                    ?> ></script>
                <div id="smartlibPlugin">
                    <div class="smartlibTitle">
                        <a href=

                           <?php
                           echo '"' . _LIB_URL . '";';
                           ?>
                                   target="_blank">
                        Smartlib Books
                        </a>
                    </div>
                    <div id="smartlib_books">
                    </div>
                </div>

                <style>
                        /* Change style of SmartLib Plugin */
                    #smartlibPlugin {
                        /*        Width of plugin, and maximum height */
                        width: 300px;
                        max-height: 300px;
                    }
                </style>
                <script>
                    <?php echo 'var user ="' . $_SESSION['username'] . '"'; ?>

                    var params = "type=json" + "&user=" + user + "&mykey=" + <?php echo "'" . md5(_MY_KEY) . "'" ?>;

                    runAWebpageForResult(
                    <?php
                    echo '"' . _LIB_URL . '/scripts/exportBooks.php' . '"'; ?>,
                            params, fillPluginBooks, <?php echo '"' . _LIB_URL . '"'; ?> );
                </script>
                <br>
                <a href="scripts/smartlibPlugin.php" target="_blank"
                   style="text-decoration: none; border: none;">
                    <button class="sendButton">Get Code</button>
                </a>

                <br><br>
            </div>




            <?php
            //If user is admin
            if ($_SESSION['level'] >= 2) {

                ?>
                <header>
                    <h1>
                        Administrator Panel
                    </h1>
                </header>
                <div class="mainContent">
                    <p>To export all Library in bibtext format, enter the following URL:</p>
                    <textarea id="bibtexAllBooks" style="height: auto; width: 100%; "><?php echo _LIB_URL;?>
                        scripts/exportBooks.php?mykey=0a05bb77edad7395da818f361f8115a2</textarea>
                    <br><br>

                    <p>To use SmartLib plugin for Library:</p>
                    <a href="scripts/smartlibPlugin.php?showallbooks=1" target="_blank"
                       style="text-decoration: none; border: none;">
                        <button class="sendButton">Get Code</button>
                    </a>
                    <br><br>
                </div>
                <?php

                if ($_SESSION['level'] == 3) {
                    //Aditionally show Owner Panel
                    ?>
                    <header>
                        <h1>
                            Owner Panel
                        </h1>
                    </header>
                    <div class="mainContent">
                        <p>To make another user Admin enter its username below:</p>

                        <div id="register-panel">
                            <div id="makeadmin-form">
                                <fieldset>
                                    <label>Username:
                                        <input value="" value=""
                                               id="makeadmin-form-username" autocomplete="false"
                                                />
                                        <span class="required">*</span>
                                    </label>
                                    </br>
                                    <button class="sendButton" type="submit"
                                            onclick="makeUserAdmin(event)"
                                            >Submit
                                    </button>
                                </fieldset>
                            </div>
                            <script>
                                $("#makeadmin-form-username").autocomplete("scripts/autocomplete/getUsernameList.php"


                                        , {
                                            matchContains:true,
//                    minLength: 2,
                                            selectFirst:false

                                        });
                            </script>
                            <br><br>
                        </div>
                    </div>
                    <?php

                }
            }
            ?>

        </article>

    </div>


</div>

<footer class=clearfix>
    <?php include "scripts/footer.php" ?>
</footer>

</body>
</html>