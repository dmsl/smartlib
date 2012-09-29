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
                <!--action="mobile/SQLregister.php" method="post"-->

                <div id="register-form">
                    <fieldset>
                        <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            Username:
                            <input value="<?php
                            echo $_SESSION['REGusername'];
                            ?>"
                                   id="register-form-username"
                                   name="username" type="text"/>
                            <span class="required">*</span>
                        </label>   </br>

                        <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            Password:
                            <input
                                    id="register-form-password"
                                    name="password" type="password"/>
                            <span class="required">*</span>
                        </label></br>


                        <label>
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            Confirm Password:
                            <input
                                    id="register-form-confPassword"
                                    name="confPassword" type="password"/>
                            <span class="required">*</span>
                        </label></br>


                        <label>
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            Name:</span>
                            <input value="<?php
                            echo $_SESSION['REGname'];
                            ?>"
                                   id="register-form-name"
                                   name="name" type="text"/>
                            <span class="required">*</span>
                        </label></br>


                        <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            Surname:</span>
                            <input value="<?php
                            echo $_SESSION['REGsurname'];
                            ?>"
                                   id="register-form-surname"
                                   name="surname" type="text"/>
                            <span class="required">*</span>
                        </label></br>


                        <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            Email:</span>
                            <input value="<?php
                            echo $_SESSION['REGemail'];
                            ?>"
                                   id="register-form-email"
                                   name="email" type="email"/>
                            <span class="required">*</span>
                        </label></br>


                        <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            Telephone:</span>
                            <input value="<?php
                            echo $_SESSION['REGtelephone'];
                            ?>"
                                   placeholder="0000-99999999" id="register-form-telephone"
                                   name="telephone" type="text"/>
                            <span class="required">*</span>
                        </label></br>


                        <label><span class="p4Black">Application Notifications:</span>
                            <input name="appNotif" type="checkbox" checked="CHECKED"
                                   id="register-form-app-notifications"
                                    />
                        </label></br>
                        <label><span class="p4Black">&nbsp&nbsp&nbsp&nbsp&nbsp
                            Email Notifications:</span>
                            <input name="emailNotif" type="checkbox"
                                   id="register-form-email-notifications"
                                    />
                        </label>
                        </br>


                        <!--                        <button href="#" class="sendButton"-->
                        <!--                           onClick="document.getElementById('register-form').submit()">Submit</button>-->

                        <button class="sendButton" type="submit"
                                onclick="registerFormSubmit(event)"
                                >Submit
                        </button>
                        <button class="clearButton"
                                onclick="registerFormReset()"
                                type="button"
                                >Clear
                        </button>
                    </fieldset>
                </div>
                <script>
                    //Initialize form fields
                    $(document).ready(function () {

                        $("#register-form-username, #register-form-password," +
                                " #register-form-confPassword, #register-form-name, #register-form-surname").
                                each(function () {
                                    updateFormFieldStatusSimple($(this))
                                });
                        ;

                        updateFormFieldStatusEmail($("#register-form-email"));

                        updateFormFieldStatusTelephone($("#register-form-telephone"));


                        updateFormFieldStatusConfirmPassword($("#register-form-confPassword"));

                        updateFormFieldStatusPassword($("#register-form-password"));


                        $("#register-form-username, #register-form-password," +
                                " #register-form-confPassword, #register-form-name," +
                                " #register-form-surname").keyup(function () {
                                    //Something is filled
                                    updateFormFieldStatusSimple(this);
                                });

                        $('#register-form-email').keyup(function () {
                            //Something is filled
                            updateFormFieldStatusEmail(this);
                        });

                        $('#register-form-telephone').keyup(function () {
                            //Something is filled
                            updateFormFieldStatusTelephone(this);
                        });

                        $('#register-form-confPassword').keyup(function () {
                            //Something is filled
                            updateFormFieldStatusConfirmPassword(this);
                        });

                        $('#register-form-password').keyup(function () {
                            //Something is filled
                            updateFormFieldStatusPassword(this);
                        });


                    });


                </script>
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