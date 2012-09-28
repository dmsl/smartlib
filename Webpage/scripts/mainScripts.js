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

                //TODO SYNC THE PHP SESSION!!!!


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
function contactFormSubmit(ev) {

    ev.target.checkValidity();

    //Gather new data
    var name = document.getElementById("contact-form-Name").value;
    var email = document.getElementById("contact-form-Email").value;
    var message = document.getElementById("contact-form-Message").value;


    var params = "CONTname=" + name + "&CONTemail=" + email
        + "&CONTmessage=" + message;

    runAPostWebpage("scripts/contactSubmit.php", params);

    return false;
}


// TODO Reset contact form
function contactFormReset() {

    runAWebpage("scripts/resetContact.php");

    $("#contact-form-Name").val("");
    $("#contact-form-Email").val("");
    $("#contact-form-Message").val("");

//    $("#contact-form-Name")._refreshValue();
//    $("#contact-form-Email")._refreshValue();
//    $("#contact-form-Message")._refreshValue();

}


/* Runs a URL webpage with Post method
 * And show a toast message if error found
 *
 * */
function runAPostWebpage(url, postdata) {


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

//            //User successfully logged in
//            if (result == "1") {
//                //Form send successfully
//                showToastMessage(, 1);
//
//            }
//           //Show Error
//            else {
//                showToastMessage(message, 0);
//            }

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