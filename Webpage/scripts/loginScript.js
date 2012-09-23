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


function hideToastMessage(){
    var alert = document.getElementById("toast-message");
    alert.style.opacity = 0;

}

function showToastMessage(message){

//    alert.style.opacity = 1;
//    var alert = document.getElementById("toast");

    $('.toast-message').replaceWith(
        "<div dis class='toast-message' id='error'>" + message+ "</div>");

    $('.toast-message').fadeIn(500);




}




/* Asychromous Login */
function asyncLogin(){

    //onClick="document.getElementById("login-widget").submit()
   // var username = document.getElementById("loginUsernameField").value;
  //  var password = document.getElementById("loginPasswordField").value;
    showToastMessage("TextToShow");




  //  document.getElementById("resultDiv").innerHTML="adsfas" ;

   /* var xmlhttp;

    //Make the request (IE7+, and browsers)
    if (window.XMLHttpRequest){
        xmlhttp=new XMLHttpRequest();
    }
    //For IE<=6
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    //When state changed
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            document.getElementById("resultdiv").innerHTML=xmlhttp.responseText ;
        }
        else if(xmlhttp.status==404){
            //Authentication script not found
            document.getElementById("resultdiv").innerHTML="script not found" ;

        }
    }

    xmlhttp.open("POST","ajax_info.txt",true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("fname=Henry&lname=Ford");//TODO
    */

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
