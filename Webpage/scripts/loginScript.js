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






/* Toggle login button according to username field changes */
function toggleLoginButton() {
    if (($('#loginUsernameField').attr('value') != "")
        &&
        ($('#loginPasswordField').attr('value') != "")
        ) {
        //Activate Login Link
        //              //            //Login is link class=\"loginButton\"
        /*                $('#login-panel-submit').replaceWith("aaaaaaaaaa <a  id=\"login-panel-submit\" \
         href=\"#\" onClick=\"document.getElementById('login-widget').submit() >Login</a>");*/

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
        // $('#login-panel-submit-filled').click();
        //TODO changed document.getElementById("login-widget").submit()
        asyncLogin();
    }


}

//Show toast message according to message type
function showToastMessage(msg) {

    $(document).ready(function () {


        var messageType = msg.substring(0, 3);
        var message = msg.substring(3);

        //Show error toast
        if (messageType == "err") {
            messageType = 'error';
        }
        //Show info toast
        else if (messageType == "inf") {
            messageType = 'info';
        }
        else {
            //Show nothing
            return;
        }


        //Show toast message
        $('.toast-message').replaceWith(
            "<div dis class='toast-message' id='" + messageType + "'>" + message + "</div>");

        $('.toast-message').fadeIn("normal", onToastClickFadeOut());


    });


}

//Adds fade out functionality to Toast Message
function onToastClickFadeOut() {
    $('.toast-message').click(function () {
        $('.toast-message').fadeOut("slow");

    });

}


/* Asychromous Login */
function asyncLogin() {

    //TODO fade and make items unclickable!
    // UNTIL USER LOGINS!

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

    xmlhttp.send(buildParams);//TODO

    //When state changed
    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            var message = xmlhttp.responseText.trim();

            //TODO MESSAGE MUST BE:
            // 1
            // pASHALIS
            // PMPEIS01
            // EMAIL etc.. ALL HAS TO GO DONT IN JS/AJAX
            //user has logged in
            if (message == '1') {
                var phpCode = "<?PHP echo \"Gergo\"; ?>";

                var string2 = "<div id='login-panel-loggedin'>" +
                    phpCode +
                    "</div>";

                //Modify login area
                $('#loginUsernameField').replaceWith(string2);


                $('#loginPasswordField').hide();



                //Login button
                $('#login-panel-submit-filled').replaceWith(
                    '<button type="submit" onclick="asyncLogout()"   id="login-panel-submit-filled" href=""  ' +
                        ' >LogouT</button>');

                //Profile Button

            }
            else {
                //Un-fade items
                showToastMessage(message);
            }


        }
        else if (xmlhttp.status == 404) {
            //Authentication script not found
            //TODO check if have todo something
            showErrorToastMessage("Webpage Error");
            //document.getElementById("resultdiv").innerHTML="script not found" ;

        }
    }


}





/* TODO Asychromous Login  */
function asyncLogout() {


}




//MAY REMOVE THIS CODE

//   var isShown = false;

//        //todo rm Slide top menu
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
