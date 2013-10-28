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
<html lang=en xmlns="http://www.w3.org/1999/html">

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
    <?php include 'scripts/gridHeader.php';
    include 'carousel/carouselHeader.php';  ?>

    <script src="scripts/mainScripts.js"></script>
</head>


<body>

<!-- Header -->
<header id='topHeader'>
    <?php  include('scripts/header.php'); ?>
</header>
<div class="mainContent">
    <!--Toast Messages -->
    <div class=clearfix>
        <div class="toast-message" id="notoast">No Errors Found</div>
        <div id=content>

            <article>
                <header>
                    <h1>Download SmartLib on your Smartphone or Tablet</h1>
                </header>
                <div class="mainContent" id="downloadsDevices">
                    <img id="qrcode" src="https://www.cs.ucy.ac.cy/projects/smartLib/images/qr.png"
                         alt="Smart QR Code"/>

                    <p>
                        SmartLib is available for Android and iOS Platform. Using SmartLib application you
                        can have access to all activated libraries.<br>
                        Additionally you can:<br>
                        add books to your library, make them rentable or not, lent them,
                        request books owned by other users, contact u users.<br><br>
                        <a class="nodec"
                           href="https://play.google.com/store/apps/details?id=cy.ac.ucy.pmpeis01.client.android"
                           target="_blank">
                            <button class="sendButton"
                                    >Android
                            </button>
                        </a>
                        <a class="nodec"
                           href="https://itunes.apple.com" target="_blank">
                            <button class="sendButton">iOS</button>
                        </a> <br>
                        <br>
                    </p>
                </div>
                <header>
                    <h1>Setup your own Library</h1>
                </header>
                <div class="mainContent">
                    <p>
                        You can setup your own SmartLib. All you need is a PHP Server, and
                        a MySQL Database.<br>Also a Mail server is optional for users private intercommunication.
                        <br><br>
                        Download the SmartLib source code from GitHub, and follow instructions
                        in Readme file. Once the installation process completes, you have to wait
                        for your library's activation from SmartLib Database Administrator.
                        <br><br>
                        <!--                        <span class="imageHandle">-->
                        <!--            <a class="nodec" href="https://github.com/dmsl/smartlib" target="_blank"><img weigth="50" src="images/github.png"-->
                        <!--                      alt="SmartLib Repository"></a></br></span>-->
                    </p>

                    <p>
                        <strong>Smartlib activity on Github:</strong><br>

                    <div style="width: 500px;">
                        <div class="github-widget" data-repo="dmsl/smartlib"></div>
                        <!--                        <br>-->
                        <div class="github-widget" data-repo="dmsl/smartlib/graphs"></div>
                    </div>
                    </p>

                </div>
                <!-- #container -->

                <script type="text/javascript" src="scripts/jquery.githubRepoWidget.min.js"></script>

        </div>
        </article>

    </div>


</div>
<footer class=clearfix>
    <?php include "scripts/footer.php" ?>

</footer>
</body>
</html>