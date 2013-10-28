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


//Save the users activation code
$useALink = $_REQUEST['uLnk'];
$usersActivationCode = $_REQUEST['activationCode'];

//User activated using link
if ($useALink == "yes") {
    //Save its username from the link
    $_SESSION['REGusername'] = $_REQUEST['uLnkUsername'];
    //Set a flag, so the activation will be legal
    $_SESSION['LnkLegalActivation'] = "Y";
} else {
    $_SESSION['REGusername'] = "";
    //Set a flag, so the activation will be legal
    $_SESSION['LnkLegalActivation'] = "";
}


?>
<script>
    var actUsername;
    var legalActivation;
    var actCode;
</script>
<?php

//User is logged in move him to its Profile Page
if ($_SESSION['loggedin'] == 1) {
    header("Location: userProfile.php");
}

echo "<script>\n" .
    "actUsername=\"" . $_SESSION['REGusername'] . "\";\n" .
    "legalActivation=\"" . $_SESSION['LnkLegalActivation'] . "\";\n" .
    "actCode=\"" . $usersActivationCode . "\";\n" .
    "</script>\n";


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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <link href=styles.css rel=stylesheet>
    </link>
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
                <h1><?php echo $_SESSION['REGusername']; ?> Account Activation</h1>
            </header>
            <div class="mainContent">
                <p>Thank you for registering to SmartLib.<br>
                    Please check your email, and activate your account.<br><br>
                    After activation, you will be able to scan your books using Smartphone application.<br>
                    Also you can lent or return your books.<br><br><br><br><br><br><br><br>
            </div>
        </article>
        <script>
            $(document).ready(function () {
                if (legalActivation != "Y") {
                    showToastMessage("Accounts are activated only using email", 0);

                }

                //Attempt to activate account
                else {
                    //Not filled informations
                    if (actCode == "" || actUsername == "") {
                        showToastMessage("Wrong Activation Details", 0);
                    }
                    else {
                        //Build params
                        var params = "activationCode=" + actCode +
                                "&uLnkUsername=" + actUsername;

                        runAPostWebpage("mobile/activateAccount.php", params, switchToActivationSuccessPage, actUsername);
                    }


                }
            });


        </script>

    </div>


</div>

<footer class=clearfix>
    <?php include "scripts/footer.php" ?>

</footer>

</body>
</html>