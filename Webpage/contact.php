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
    <link href="images/favicon.ico" rel="icon" type="image/x-icon"/>
    <!--    Include Scripts-->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <link href=styles.css rel=stylesheet>
    </link>
    <!--    Include Extra headers-->
    <?php include 'scripts/gridHeader.php';
    include 'carousel/carouselHeader.php';  ?>
    <script src="scripts/mainScripts.js "></script>

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
            <div class="mainContent" id="contactContent">

                <!--                Contact Information-->
                <aside>
                    <h1 class="centered">Office Information</h1>

                    <p class="left">
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
                <div id="contact-panel">
                    <div id="contact-form">
                        <fieldset>
                            <label>&nbsp&nbsp&nbspName:
                                <input name="CONTname" id="contact-form-Name"
                                       type="text" autofocus="autofocus"
                                       placeholder="Your name"
                                       value="<?php
                                       if ($_SESSION['loggedin' == 1]) {
                                           if ($_SESSION['CONTname'] == "")
                                               $_SESSION['CONTname'] = $_SESSION['name'];

                                       }
                                       echo $_SESSION['CONTname'];
                                       ?>"/>
                                <span class="required">*</span>
                            </label><br>
                            <label>&nbsp&nbspEmail:
                                <input
                                        name="CONTemail" type="email" id="contact-form-Email"
                                        placeholder="me@example.com"
                                        value="<?php
                                        if ($_SESSION['loggedin' == 1]) {
                                            if ($_SESSION['CONTemail'] == "")
                                                $_SESSION['CONTemail'] = $_SESSION['email'];
                                        }
                                        echo $_SESSION['CONTemail'];
                                        ?>"/>
                                <span class="required">*</span></label>

                            <br>
                            <label>Message:&nbsp
                                <span class="required">*</span></label>
                            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                <textarea cols=40 rows=7 name="CONTmessage"
                                          id="contact-form-Message"
                                          placeholder="Your message..."
                                "
                                ><?php
                                echo $_SESSION['CONTmessage'];
                                ?></textarea>
                            </label>
                            <br>
                            <button class="sendButton" type="button"
                                    onclick="contactFormSubmit(event)"
                                    >Send
                            </button>
                            <button class="clearButton" type="button"
                                    onclick="contactFormReset()"
                                    >Clear
                            </button>
                        </fieldset>
                    </div>
                    <script>
                        //Initialize form fields
                        $(document).ready(function () {

                            $("#contact-form-Name, #contact-form-Email, #contact-form-Message").
                                    each(function () {
                                        updateFormFieldStatusSimple($(this))
                                    });


                            updateFormFieldStatusEmail($("#contact-form-Email"));


                            $('#contact-form-Name, #contact-form-Message ').keyup(function () {
                                //Something is filled
                                updateFormFieldStatusSimple(this);
                            });

                            $('#contact-form-Email').keyup(function () {
                                //Something is filled
                                updateFormFieldStatusEmail(this);
                            });


                        });
                    </script>
                </div>


            </div>
        </article>

    </div>


</div>
<div class="footerPusher"></div>
<footer class=clearfix>
    <?php include "scripts/footer.php" ?>
</footer>

</body>
</html>