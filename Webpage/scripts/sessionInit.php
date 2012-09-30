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


require_once("CONFIG.php");
//Connect to Database
include('dbConnect.php');
// TODO change this NOTIFICATION like method!
//Check if we have message to display to user
if (isset($_SESSION['topTypeMsg'])) {
    //There is something wrong in the site
    if ($_SESSION['topTypeMsg'] == "err") {
        echo "<strong class= \"errorTopMsg\">" . $_SESSION['topMsg'] . "</br> </strong>";
    } else if ($_SESSION['topTypeMsg'] == "info") {
        echo "<strong class= \"infoTopMsg\">" . $_SESSION['topMsg'] . "</br> </strong>";
    }

    //Unset those messages for no future re-show
    unset($_SESSION['topTypeMsg']);

}


//Save the Page we are currently at
$_SESSION['currentPage'] = 'http' .
    (empty($_SERVER['HTTPS']) ? '' : 's') .
    '://' . $_SERVER['SERVER_NAME'] .
    $_SERVER['REQUEST_URI'];


?>


