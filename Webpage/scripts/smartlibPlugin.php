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

$showAllBooks = $_GET['showallbooks'];

require_once("../CONFIG.php");
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
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <!--    Include Scripts-->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <link href="../styles.css" rel=stylesheet>
    </link>

    <script src="mainScripts.js"></script>
</head>

<body>
<div class=clearfix>
    <div id=content>
        <article>
            <header>
                <h1>Include this code on your Webpage:</h1>
            </header>
            <div class="mainContent">
                <textarea id="pluginBook" style="width: 700px;">
                    <link <?php
                        echo 'href="' . _LIB_URL . '/scripts/smartPlugin.css"';
                        ?> rel=stylesheet/>
                    <script <?php
                        echo 'src="' . _LIB_URL . '/scripts/mainScripts.js"';
                        ?>></script>
                    <div id="smartlibPlugin">
                        <div class="smartlibTitle">
                            <a href= <?php echo '"' . _LIB_URL . '";';?> target="_blank"> Smartlib Books</a>
                        </div>
                        <div id="smartlib_books"></div>
                    </div>

                    <style>
                            /* Change style of SmartLib Plugin */
                        #smartlibPlugin {
                            /* Width of plugin, and maximum height */
                            width: 300px;
                            max-height: 500px;
                        }

                        .smartlibTitle {
                            /* Change title style */
                        }
                    </style>

                    <script>
                        <?php
                        if ($showAllBooks != "1") {
                            echo 'var user ="' . $_SESSION['username'] . '";';
                        }?>

                        var params = "type=json" <?php
                        if ($showAllBooks != "1") {
                            echo '+ "&user=" + user';
                        }   ?>+ "&mykey=" + <?php echo "'" . md5(_MY_KEY) . "'" ?>;
                        runAWebpageForResult(<?php
                        echo '"' . _LIB_URL . '/scripts/exportBooks.php"';
                        ?>, params, fillPluginBooks, <?php echo '"' . _LIB_URL . '"'; ?> );
                    </script>
                </textarea>

            </div>
        </article>

    </div>


</div>
</div>
</body>
</html>

