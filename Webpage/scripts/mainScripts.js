/**
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

// User Variables

var _username;
var _name;
var _surname;
var _email;
var _telephone;
var _allowrequests;
var _level;
var _loginResult;


/* Toggle login button according to username field changes */
function toggleLoginButton() {
    if (($('#loginUsernameField').attr('value') != "")
        &&
        ($('#loginPasswordField').attr('value') != "")
        ) {

        //Activate Login Link
        $('#login-panel-submit').replaceWith(
            '<button type="submit" onclick="asyncLogin()"   id="login-panel-submit-filled" href=""  ' +
                ' >Login</button>');


    }
    else {
        //Deactivate Login Link
        //Login is not link
        $('#login-panel-submit-filled').replaceWith(
            "<div id=\"login-panel-submit\">Login</div>");
    }

}


/* Toggle login button according to password field changes */
function toggleLoginButtonPassword(event) {
    //Toggle Login Button
    toggleLoginButton();
    if (event.keyCode == 13) {
        asyncLogin();
    }

}


//Show toast message according to message type
function showToastMessage(msg, type) {

    $(document).ready(function () {


        var messageType;// = msg.substring(0, 3);
        // var message = msg.substring(3);

        //Show error toast
        if (type == "0") {
            messageType = 'error';
        }
        //Show info toast
        else if (type == "1") {
            messageType = 'info';
        }
        else {
            //Show nothing
            return;
        }


        //Show toast message
        $('.toast-message').replaceWith(
            "<div dis class='toast-message' id='" + messageType + "'>" + msg + "</div>");

        $('.toast-message').fadeIn("normal", function () {

            //Hide on click
            $('.toast-message').click(function () {
                $('.toast-message').clearQueue();
                $('.toast-message').fadeOut("slow");

            });

            //Auto hide
            $('.toast-message').delay(5000);
            $('.toast-message').fadeOut("slow");


        });


    });


}


/* Async Login */
function asyncLogin() {

    //TODO FUTURE fade and make items unclickable!
    // UNTIL USER LOGINS! FINISHES!!

    //onClick="document.getElementById("login-widget").submit()
    var username = document.getElementById("loginUsernameField").value;
    var password = document.getElementById("loginPasswordField").value;


    var buildParams = "username=" + username + "&password=" + password;


    var xmlhttp;

    //Make the request (IE7+, and browsers)
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    //For IE<=6
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }


    xmlhttp.open("POST", "mobile/authenticate.php", true);
    //Send the proper header information along with the request


    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send(buildParams);

    //When state changed
    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            var jsonString = xmlhttp.responseText.trim();


            var jsonOBJ = eval('(' + jsonString + ')');

            _loginResult = jsonOBJ['result'];

            //If result is wrong, save the message
            var message = jsonOBJ['message'];

            _username = jsonOBJ['username'];
            _name = jsonOBJ['name'];
            _surname = jsonOBJ['surname'];
            _email = jsonOBJ['email'];
            _telephone = jsonOBJ['telephone'];
            _allowrequests = jsonOBJ['allowRequests'];
            _level = jsonOBJ['level'];


            //User successfully logged in
            if (_loginResult == "1") {

                // User successfully logged in, and everything is okay!
                if (_level == "1" || _level == "2") {
                    //Admin logged in!
                    if (_level == "2") {
                        showToastMessage("Welcome ADMIN.", 1);
                    }


                    //Modify login area
                    $('#loginUsernameField').replaceWith(
                        "<div id='login-panel-loggedin'>" +
                            _name +
                            "</div>"

                    );


                    $('#loginPasswordField').hide();

                    //Login button
                    $('#login-panel-submit-filled').replaceWith(
                        '<button type="submit" onclick="asyncLogout()"   id="login-panel-submit-logout" href=""  ' +
                            ' >Logout' + '</button>');


                    //Switch menu links
                    $('#navRegister').replaceWith(
                        '<a id="navUserProfile" href="userProfile.php">Profile</a>'
                    );


                    //Do actions according to current page
                    var curPage = window.location.pathname;
                    var curPage = curPage.substring(curPage.lastIndexOf('/') + 1);
                    //If on homepage, switch books
                    if (curPage == "index.php" || curPage == "") {
                        //Replace books with logged in books
                        // TODO CHECK getLoggedInBooksJqGrid();
                        window.location.reload();
                    }
                    //Refresh Page
                    else if (curPage == "register.php") {
                        window.location.reload();
                    }


                }

                //Not activated account
                else if (_level == "0") {

                    showToastMessage(_name + ", your account is not activated.</br>" +
                        "Activate it using your email: " + _email
                        , 0);

                }
                //User is visitor
                else if (_level == "-1") {
                    showToastMessage(_name + ", you are a visitor to SmartLib.</br>" +
                        "Please make a regular account."
                        , 0);

                }
                //User is banned
                else if (_level == "-2") {
                    showToastMessage(_name + ", you are banned from SmartLib</br>" +
                        "Contact SmartLib Admin for further Details."
                        , 0);

                }

            }
            //Wrong username and/or password
            else if (_loginResult == "0") {
                showToastMessage(
                    "Wrong Username or Password.</br>" +
                        "Please try again."
                    , 0)
            }
            //Database error | -11 code
            else {
                showToastMessage("Database Error :(", 0);
            }

        }
        //Error Code
        else if (xmlhttp.status == 404) {

            //Authentication script not found
            showToastMessage("Webpage Error :(", 0);

        }


    }

}


/* Async Logout  */
function asyncLogout() {

    var xmlhttp;

    //Make the request (IE7+, and browsers)
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    //For IE<=6
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }


    xmlhttp.open("GET", "scripts/logout.php", true);
    //Send the proper header information along with the request
    xmlhttp.send();

    //When state changed
    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            var jsonString = xmlhttp.responseText.trim();


            var jsonOBJ = eval('(' + jsonString + ')');

            _logoutResult = jsonOBJ['result'];


            //User successfully logged in
            if (_logoutResult == "1") {
                $('#login-panel').replaceWith(
                    '<div id="login-panel">\
                    <input type="text" name="username" placeholder="Username"\
                    autocomplete="off"\
                    autofocus="autofocus"\
                    id="loginUsernameField"\
                    onkeyup="toggleLoginButton()"\
                    />\
                    <input type="password" name="password" placeholder="Password"\
                onkeyup="toggleLoginButtonPassword(event)"\
                id="loginPasswordField"\
                autocomplete="off"  />\
                    <!--        Login Button-->\
                    <div  id="login-panel-submit">Login</div>\
                    </div>');

                //Switch menu links
                $('#navUserProfile').replaceWith(
                    '<a id="navRegister" href="register.php">Register</a>'
                );


                //Do actions according to current page
                var curPage = window.location.pathname;
                var curPage = curPage.substring(curPage.lastIndexOf('/') + 1);

                //If on homepage, switch books
                if (curPage == "index.php" || curPage == "") {
                    //Replace books with logged out books
                    //TODO CHECK getLoggedOutBooksJqGrid();
                    window.location.reload();
                }
                //Refresh Page
                else if (curPage == "userProfile.php") {
                    window.location.reload();
                }


            }
            // Weird error
            else {
                showToastMessage("Something went wrong :(", 0);
            }

        }



        //Error Code
        else if (xmlhttp.status == 404) {

            //Authentication script not found
            showToastMessage("Webpage Error :(", 0);

        }

    }

}


/* Get logged in books JQ Grid  */
function getLoggedOutBooksJqGrid() {

    var xmlhttp;


    //Make the request (IE7+, and browsers)
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    //For IE<=6
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }


    xmlhttp.open("GET", "grid/allBooks.php", true);
    //Send the proper header information along with the request
    xmlhttp.send();

    //When state changed
    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            var result = xmlhttp.responseText.trim();

            //Enable logged in books
            $('.mainContent#booksLoggedIn').replaceWith(
                '<div class="mainContent" id="booksNotLoggedIn" >' +
                    result +
                    '</div>'
            );


        }

        //Error Code
        else if (xmlhttp.status == 404) {

            //Authentication script not found
            showToastMessage("Webpage Error :(", 0);

            // return "Webpage Error :(";

        }


    }


}


/* Get logged in books JQ Grid  */
function getLoggedInBooksJqGrid() {

    var xmlhttp;


    //Make the request (IE7+, and browsers)
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    //For IE<=6
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }


    xmlhttp.open("GET", "grid/allBooksLoggedIn.php", true);
    //Send the proper header information along with the request
    xmlhttp.send();

    //When state changed
    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            var result = xmlhttp.responseText.trim();

            //Enable logged in books
            $('.mainContent#booksNotLoggedIn').replaceWith(
                '<div class="mainContent" id="booksLoggedIn" >' +
                    result +
                    '</div>'
            );


        }


        //Error Code
        else if (xmlhttp.status == 404) {

            //Authentication script not found
            showToastMessage("Webpage Error :(", 0);

            // return "Webpage Error :(";

        }


    }


}


//MAY REMOVE THIS CODE

//   var isShown = false;

//        //TODO SLIDE UP DOWN CODE! use this on search fields!
//        $(document).ready(function () {
//            $('#sub-menu-panel').click(function () {
//                //Show panel
//                if (!isShown) {
//                    $('#login-panel').slideDown("slow");
//                    isShown = true;
//                }
//                //Hide panel
//                else {
//
//
//                    $('#login-panel').slideUp("slow");
//                    isShown = false;
//                }
//
//
//            });
//            $('#login-panel').click(function () {
//
//
//            });
//
//
//        });
//



// Submit Contact form
function registerFormSubmit(ev) {

//    //Gather new data
    var username = document.getElementById("register-form-username").value;
    var password = document.getElementById("register-form-password").value;
    var confPassword = document.getElementById("register-form-confPassword").value;
    var name = document.getElementById("register-form-name").value;
    var surname = document.getElementById("register-form-surname").value;
    var email = document.getElementById("register-form-email").value;
    var telephone = document.getElementById("register-form-telephone").value;
    var emailNotifications = document.getElementById("register-form-email-notifications").value;
    var appNotifications = document.getElementById("register-form-app-notifications").value;


    //Some fields still empty
    if (username == "" || password == "" || confPassword == ""
        || name == "" || surname == "" || email == "" || telephone == "") {
        showToastMessage("Please fill all required form fields", 0);
        return false;
    }

    var hasError = false;
    var msg = "";

    if (!isEmailValid(email)) {
        msg += "Invalid email address";
        hasError = true;
    }

    if (!isTelephoneValid(telephone)) {
        if (msg != "") msg += "</br>";

        msg += "Invalid telephone number";
        hasError = true;
    }

    if (password != confPassword) {
        if (msg != "") msg += "</br>";
        msg += "Passwords don't match";
        hasError = true;
    }

    if (hasError) {
        showToastMessage(msg, 0);
        return false;
    }


    //Send post request

    var params = "username=" + username + "&password=" + password
        + "&confPassword=" + confPassword
        + "&name=" + name
        + "&surname=" + surname
        + "&email=" + email
        + "&telephone=" + telephone
        + "&emailNotifications=" + emailNotifications
        + "&appNotifications=" + appNotifications;

    runAPostWebpage("mobile/SQLregister.php", params, switchToRegistrationSuccessPage);

    return false;
}


function switchToRegistrationSuccessPage() {
    $("#register-panel").replaceWith('<div id="register-success"><p>bravo!</p></div>');

}


// Submit Contact form
function contactFormSubmit() {

    //Gather new data
    var name = document.getElementById("contact-form-Name").value;
    var email = document.getElementById("contact-form-Email").value;
    var message = document.getElementById("contact-form-Message").value;


    //Check if user filled data
    if (name == "" || email == "" || message == "") {
        showToastMessage("Please fill all all form fields", 0);
        return false;
    }


    //Check email
    if (!isEmailValid(email)) {
        showToastMessage("Invalid email address", 0);
        return false;
    }


    var params = "CONTname=" + name + "&CONTemail=" + email
        + "&CONTmessage=" + message;

    runAPostWebpage("scripts/contactSubmit.php", params, "");

    return false;
}


// Reset register form
function registerFormReset() {

    //Clear PHP data
    runAWebpage("scripts/resetRegister.php");


    $("#register-form").find('input:text, input').val("");
    $("#register-form-app-notifications").prop("checked", true);
    $("#register-form-email-notifications").prop("checked", false);

    $("#register-form-username, #register-form-password," +
        " #register-form-confPassword, #register-form-name," +
        " #register-form-surname, #register-form-email, #register-form-telephone, " +
        "#register-form-confPassword, register-form-password"
    ).
        each(function () {
            updateFormFieldStatusSimple($(this))
        });


}


// Reset contact form
function contactFormReset() {



    //Clear PHP data
    runAWebpage("scripts/resetContact.php");

    $("#contact-form").find('input:text, input, textarea').val("");
    $("#contact-form-Name, #contact-form-Email, #contact-form-Message").
        each(function () {
            updateFormFieldStatusSimple($(this))
        });

}


/* Runs a URL webpage with Post method
 * And show a toast message if error found
 * and run a method if any
 *
 * */
function runAPostWebpage(url, postdata, successCallback) {


    var xmlhttp;

    //Make the request (IE7+, and browsers)
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    //For IE<=6
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.open("POST", url, true);

    //Send the proper header information along with the request
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send(postdata);


    //When state changed
    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            var jsonString = xmlhttp.responseText.trim();

            var jsonOBJ = eval('(' + jsonString + ')');

            var result = jsonOBJ['result'];

            var message = jsonOBJ['message'];

            showToastMessage(message, result);

            //On success
            if (result == 1) {
                if (successCallback != "") {
                    successCallback();
                }
            }


        }

        //Error Code
        else if (xmlhttp.status == 404) {

            //Authentication script not found
            showToastMessage("Webpage Error :(", 0);

        }

    }

}


//Runs a webpage with AJAX w/o showing a success message
function runAWebpage(url) {

    var xmlhttp;

    //Make the request (IE7+, and browsers)
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    //For IE<=6
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }


    xmlhttp.open("GET", url, true);
    //Send the proper header information along with the request
    xmlhttp.send();

    //When state changed
    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

        }

        //Error Code
        else if (xmlhttp.status == 404) {
            showToastMessage("Webpage Error :(", 0);
        }


    }

}

////////////
//////////// Help functions
////////////

//Check if a email is valid
function isEmailValid(email) {

    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

    return (reg.test(email));
}


//Check if a telephone is valid
function isTelephoneValid(tel) {

    var reg = /^((\+[1-9]{3,4}|0[1-9]{4}|00[1-9]{3})\-?)?[0-9]{8,20}$/;

    return (reg.test(tel));
}


//Updates the status according of forms fields to data filled
function updateFormFieldStatusSimple(that) {
    //Something is filled
    if ($(that).val() != "") {
        $(that).addClass('filled').removeClass('empty');
    }
    else {
        $(that).addClass('empty').removeClass('filled');
    }

}

//Updates the status according of forms Email fields to data filled
function updateFormFieldStatusEmail(that) {
    //If is empty
    if ($(that).val() == "") {
        $(that).addClass('empty').removeClass('filled').removeClass('error');
    }
    else {
        //Check if its a valid email address
        if (isEmailValid($(that).val())) {
            $(that).addClass('filled').removeClass('empty').removeClass('error');
        }
        else {
            $(that).addClass('error').removeClass('empty').removeClass('filled');
        }

    }

}


//Updates the status according of forms Email fields to data filled
function updateFormFieldStatusTelephone(that) {
    //If is empty
    if ($(that).val() == "") {
        $(that).addClass('empty').removeClass('filled').removeClass('error');
    }
    else {
        //Check if its a valid email address
        if (isTelephoneValid($(that).val())) {
            $(that).addClass('filled').removeClass('empty').removeClass('error');
        }
        else {
            $(that).addClass('error').removeClass('empty').removeClass('filled');
        }

    }

}


//Updates the status according of forms Password field
function updateFormFieldStatusPassword(that) {

    var pass = $(that).val();
    var conf = $("#register-form-confPassword").val();

    //If is empty
    if (pass == "") {
        //Mark empty
        $(that).addClass('empty').removeClass('filled').removeClass('error');

        //Confirmation is wrong
        if (conf != "")
            $("#register-form-confPassword").addClass('error').removeClass('empty').removeClass('filled');

        return;
    }


    //Passwords match: mark filled
    if (conf == pass) {
        $(that).addClass('filled').removeClass('empty').removeClass('error');
        $("#register-form-confPassword").addClass('filled').removeClass('empty').removeClass('error');
        return;
    }

    //Conf isnt wrong yet
    if (conf == "") return;

    // Confirmation password is wrong
    $("#register-form-confPassword").addClass('error').removeClass('empty').removeClass('filled');

}


//Updates the status according of forms Confirm Password field
function updateFormFieldStatusConfirmPassword(that) {

    var confPass = $(that).val();
    //If is empty
    if (confPass == "") {
        $(that).addClass('empty').removeClass('filled').removeClass('error');
        return;
    }

    var pass = $("#register-form-password").val();

    //Password is filled
    if (pass != "") {

        if (confPass == pass) {
            $(that).addClass('filled').removeClass('empty').removeClass('error');
            return;
        }


    }
    // Confirmation password is wrong
    $(that).addClass('error').removeClass('empty').removeClass('filled');

}






