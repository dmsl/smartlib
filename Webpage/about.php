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
                <h1>SmartLib Open Source Project</h1>
            </header>
            <p>SmartLib Goal is to provide users an
                easy way to share their personal book library with others,
                voluntary. Users first have to create an account to
                their desired SmartLib.</br>
                Acounts can created using library's webpage or Smartphone
                Application.</br></br>
                Organizations can create their own SmartLib, using the Project Sources
                for webpage and database, hosted on GitHub.
                Those sources are under the General Public Licence.
                <br>More information about Smartphone applications, and SmartLib's
                installation process can be found on Download section.</p>
            <br><br>
            <header>
                <h1>Project Members</h1>
            </header>
            <p>
      <span class="textHandle">
             <strong>Demetris Zeinalipour:<br>

                 PhD Lecturer at University of Cyprus</strong><br>
           &nbsp;&nbsp;&nbsp;&nbsp;
            Project Supervisor</span><br>

                <br><br> <span class="textHandle">
            <strong>Paschalis Mpeis:<br>
                BSc 4th at University of Cyprus</strong><br>
                       &nbsp;&nbsp;&nbsp;&nbsp;
            Requirement Analysis<br>
                       &nbsp;&nbsp;&nbsp;&nbsp;
            Web Interface (HTML5/PHP/JS)<br>
                       &nbsp;&nbsp;&nbsp;&nbsp;
            Database (MySQL)<br>
                       &nbsp;&nbsp;&nbsp;&nbsp;
            Smartlib API for Smartphone Clients(PHP/MySQL)<br>
                       &nbsp;&nbsp;&nbsp;&nbsp;
            Android Client (Java)</span><br>
                <br><br> <span class="textHandle">
            <strong>Chrysovalantis Anastasiou:<br>
                BSc 2nd at University of Cyprus</strong><br>
                       &nbsp;&nbsp;&nbsp;&nbsp;
            iOS Client (Objective C)</span><br>
                <br><br> <span class="textHandle">
             <strong>Chrystalla Tsoutsouki:<br>
                 BSc 2nd at University of Cyprus</strong><br>
                       &nbsp;&nbsp;&nbsp;&nbsp;
            iOS Client (Objective C)</span><br>
                <br><br>


        </article>

    </div>


</div>

<footer class="clearfix">
    <?php include "scripts/footer.php" ?>
</footer>

</body>
</html>